package com.n256coding.crazyalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class alarmHome extends AppCompatActivity {
    Button btnAlarmHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_home);

        btnAlarmHome = findViewById(R.id.btn_alarm_home);

        btnAlarmHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(alarmHome.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
