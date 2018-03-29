package com.n256coding.crazyalarm.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.n256coding.crazyalarm.AlarmEditorActivity;
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

    public void addItemToList(Alarm alarm){
        this.alarmList.add(alarm);
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.ViewHolder holder, int position) {
        final Alarm alarm = alarmList.get(position);
        holder.tvAlarmTime.setText(DateEx.getTimeString(alarm.getAlarmTime()));
        holder.itemView.setTag(alarm.getAlarmId());

        if(alarm.getAlarmStatus().equals(Alarm.ENABLED)){
            holder.itemView.setBackgroundColor(android.graphics.Color.argb(50, 100, 100, 100));
            holder.btnAlarmStatus.setVisibility(View.VISIBLE);
        }else{
            holder.itemView.setBackgroundColor(android.graphics.Color.argb(0, 100, 100, 100));
            holder.btnAlarmStatus.setVisibility(View.INVISIBLE);
        }

        holder.btnAlarmStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm.disableAlarm(view.getContext().getApplicationContext(), alarm.getAlarmId());
                Alarm tempAlarm = alarm;
                tempAlarm.setAlarmStatus(Alarm.DISABLED);
                alarmList.set(holder.getAdapterPosition(), tempAlarm.getClone());
                tempAlarm = null;
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int alarmId = alarmList.get(holder.getAdapterPosition()).getAlarmId();
                Intent intent = new Intent(view.getContext().getApplicationContext(), AlarmEditorActivity.class);
                intent.putExtra("oldAlarmId", alarmId);
                view.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
                            alarmList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlarmTime;
        Button btnAlarmStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAlarmTime = itemView.findViewById(R.id.tv_alarmTime);
            btnAlarmStatus = itemView.findViewById(R.id.btnAlarmStatus);




        }
    }
}
