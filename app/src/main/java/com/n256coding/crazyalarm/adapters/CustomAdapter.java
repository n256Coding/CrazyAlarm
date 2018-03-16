package com.n256coding.crazyalarm.adapters;

import android.app.TimePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.n256coding.crazyalarm.R;
import com.n256coding.crazyalarm.helper.DateEx;
import com.n256coding.crazyalarm.model.Alarm;

import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Alarm> alarmList;

    public CustomAdapter(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
        holder.tvAlarmTime.setText(DateEx.getTimeString(alarmList.get(position).getAlarmTime()));
        holder.itemView.setTag(alarmList.get(position).getAlarmId());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlarmTime;
        Switch swAlarmStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAlarmTime = itemView.findViewById(R.id.tv_alarmTime);
            swAlarmStatus = itemView.findViewById(R.id.sw_alarmStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog timePicker = new TimePickerDialog(view.getContext(),
                            1,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                    Alarm oldAlarm = alarmList.get(getAdapterPosition());
                                    Alarm newAlarm = alarmList.get(getAdapterPosition());
                                    newAlarm.setAlarmTime(DateEx.getAlarmDateOf(hourOfDay, minute));
                                    alarmList.set(getAdapterPosition(), oldAlarm);
                                    Alarm.changeAlarm(timePicker.getContext(), newAlarm, oldAlarm);
                                    Toast.makeText(timePicker.getContext(), "Alarm Changed", Toast.LENGTH_LONG).show();
                                    notifyItemChanged(getAdapterPosition());
                                }
                            },
                            DateEx.getCurrentHourOfDay(),
                            DateEx.getCurrentMinute(), false
                    );
                    timePicker.show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.alarm_item_context_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if(menuItem.getItemId() == R.id.menuItemRemove){
                                Alarm.removeAlarm(view.getContext(),
                                        Integer.parseInt(String.valueOf(view.getTag())));
                                alarmList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    return false;
                }
            });


        }
    }
}
