package com.n256coding.crazyalarm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.n256coding.crazyalarm.helper.MathEquation;
import com.n256coding.crazyalarm.model.Alarm;
import com.n256coding.crazyalarm.services.AlarmService;

import java.io.IOException;


public class AlarmViewerActivity extends AppCompatActivity {
    TextView tvEquation;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    MathEquation mathEquation;
    String correctAnswerHint, wrongAnswerHint;
    AlarmService alarmService;
    boolean isServiceBounded;
    private static final String TAG = "AlarmViewerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_viewer);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        tvEquation = findViewById(R.id.tv_Equation);
        btnAnswer1 = findViewById(R.id.btn_ans1);
        btnAnswer2 = findViewById(R.id.btn_ans2);
        btnAnswer3 = findViewById(R.id.btn_ans3);
        btnAnswer4 = findViewById(R.id.btn_ans4);
        mathEquation = new MathEquation();
        correctAnswerHint = "You are genius!, Lets wake up";
        wrongAnswerHint = "You cannot giveaway!, Try .....";

        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        fillEquationInfo();
    }

    public void answerBtnClicked(View view){
        Button answerBtn = (Button) view;
        if (answerBtn.getText().equals(String.valueOf(mathEquation.getCorrectAnswer()))) {
            Toast.makeText(AlarmViewerActivity.this, correctAnswerHint, Toast.LENGTH_LONG).show();
            if (isServiceBounded) {
                alarmService.stopAlarmSound();
                alarmService.removeNotification();
            }
            Intent intent = new Intent(AlarmViewerActivity.this, AlarmService.class);
            Alarm alarm = (Alarm) getIntent().getExtras().get("alarm");
            Alarm.updateAlarmStatusById(getApplicationContext(), Alarm.DISABLED, alarm.getAlarmId());
            stopService(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), wrongAnswerHint, Toast.LENGTH_SHORT).show();
            fillEquationInfo();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        isServiceBounded = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(AlarmViewerActivity.this, AlarmService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Intent intent = new Intent(AlarmViewerActivity.this, AlarmService.class);
        //stopService(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Solve the puzzle!", Toast.LENGTH_LONG).show();
    }

    private void startAlarmSound() throws IOException {
        if (isServiceBounded) {
            alarmService.startAlarmSound();
        }
    }

    private void fillEquationInfo() {
        mathEquation.refreshEquation();
        tvEquation.setText(mathEquation.toString().concat(" = ?"));
        btnAnswer1.setText(String.valueOf(mathEquation.getAnswerChoice(0)));
        btnAnswer2.setText(String.valueOf(mathEquation.getAnswerChoice(1)));
        btnAnswer3.setText(String.valueOf(mathEquation.getAnswerChoice(2)));
        btnAnswer4.setText(String.valueOf(mathEquation.getAnswerChoice(3)));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlarmService.AlarmServiceBinder binder = (AlarmService.AlarmServiceBinder) iBinder;
            alarmService = binder.getService();
            isServiceBounded = true;
            try {
                startAlarmSound();
            } catch (IOException e) {
                Log.e(TAG, "Error while playing alarm", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            alarmService = null;
            isServiceBounded = false;
        }
    };

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
