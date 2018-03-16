package com.n256coding.crazyalarm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.n256coding.crazyalarm.helper.MathEquation;
import com.n256coding.crazyalarm.services.AlarmService;


public class AlarmViewerActivity extends AppCompatActivity {
    TextView tvEquation;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    MathEquation mathEquation;
    String correctAnswerHint, wrongAnswerHint;
    AlarmService alarmService;
    boolean isServiceBounded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_viewer);

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
            stopService(intent);
            finish();
        } else {
            Toast.makeText(AlarmViewerActivity.this, wrongAnswerHint, Toast.LENGTH_LONG).show();
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
        Toast.makeText(AlarmViewerActivity.this, "Solve the puzzle!", Toast.LENGTH_LONG).show();
    }

    private void startAlarmSound(){
        if (isServiceBounded) {
            alarmService.startAlarmSound();
        }
    }

    private void fillEquationInfo() {
        mathEquation.refreshEquation();
        tvEquation.setText(mathEquation.toString().concat(" = ?"));
        btnAnswer1.setText(String.valueOf(mathEquation.getAnswerChoices()[0]));
        btnAnswer2.setText(String.valueOf(mathEquation.getAnswerChoices()[1]));
        btnAnswer3.setText(String.valueOf(mathEquation.getAnswerChoices()[2]));
        btnAnswer4.setText(String.valueOf(mathEquation.getAnswerChoices()[3]));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AlarmService.AlarmServiceBinder binder = (AlarmService.AlarmServiceBinder) iBinder;
            alarmService = binder.getService();
            isServiceBounded = true;
            startAlarmSound();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            alarmService = null;
            isServiceBounded = false;
        }
    };
}
