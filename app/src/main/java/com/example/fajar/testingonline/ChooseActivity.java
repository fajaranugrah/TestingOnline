package com.example.fajar.testingonline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity {

    private Button sd;
    private Button smp;
    private Button sma;
    private Button umum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        sd = (Button) findViewById(R.id.button5);
        smp = (Button) findViewById(R.id.button6);
        sma = (Button) findViewById(R.id.button7);
        umum = (Button) findViewById(R.id.button8);
        final Context context = this;

        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == sd){
                    Intent i = new Intent(context, QuizSDActivity.class);
                    startActivity(i);
                }
            }
        });

        smp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == smp){
                    Intent i = new Intent(context, QuizSMPActivity.class);
                    startActivity(i);
                }
            }
        });

        sma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == sma){
                    Intent i = new Intent(context, QuizSMAActivity.class);
                    startActivity(i);
                }
            }
        });

        umum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == umum){
                    Intent i = new Intent(context, QuizGeneralActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
