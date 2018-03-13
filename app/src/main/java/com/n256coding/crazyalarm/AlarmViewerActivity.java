package com.n256coding.crazyalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.n256coding.crazyalarm.helper.MathEquation;


public class AlarmViewerActivity extends AppCompatActivity {
    TextView tvEquation;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    MathEquation mathEquation;
    String correctAnswerHint, wrongAnswerHint;

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

        fillEquationInfo();

        btnAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAnswer1.getText().equals(String.valueOf(mathEquation.getCorrectAnswer()))){
                    Toast.makeText(AlarmViewerActivity.this, correctAnswerHint, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(AlarmViewerActivity.this, wrongAnswerHint, Toast.LENGTH_LONG).show();
                    fillEquationInfo();
                }
            }
        });

        btnAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAnswer2.getText().equals(String.valueOf(mathEquation.getCorrectAnswer()))){
                    Toast.makeText(AlarmViewerActivity.this, correctAnswerHint, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(AlarmViewerActivity.this, wrongAnswerHint, Toast.LENGTH_LONG).show();
                    fillEquationInfo();
                }
            }
        });

        btnAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAnswer3.getText().equals(String.valueOf(mathEquation.getCorrectAnswer()))){
                    Toast.makeText(AlarmViewerActivity.this, correctAnswerHint, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(AlarmViewerActivity.this, wrongAnswerHint, Toast.LENGTH_LONG).show();
                    fillEquationInfo();
                }
            }
        });

        btnAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAnswer4.getText().equals(String.valueOf(mathEquation.getCorrectAnswer()))){
                    Toast.makeText(AlarmViewerActivity.this, correctAnswerHint, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(AlarmViewerActivity.this, wrongAnswerHint, Toast.LENGTH_LONG).show();
                    fillEquationInfo();
                }
            }
        });
    }

    private void fillEquationInfo(){
        mathEquation.refreshEquation();
        tvEquation.setText(mathEquation.toString().concat(" = ?"));
        btnAnswer1.setText(String.valueOf(mathEquation.getAnswerChoices()[0]));
        btnAnswer2.setText(String.valueOf(mathEquation.getAnswerChoices()[1]));
        btnAnswer3.setText(String.valueOf(mathEquation.getAnswerChoices()[2]));
        btnAnswer4.setText(String.valueOf(mathEquation.getAnswerChoices()[3]));
    }
}
