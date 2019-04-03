package com.example.fajar.testingonline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AbsRuntimePermission {

    private static final int REQUEST_PERMISSION = 30000;

    private Button btnQuiz;
    private Button btnMakeQuiz;
    private Button btnStatistic;
    private Button btnProfile;
    private Button btnLogout;

    private SessionManager session;
    private SQLiteHandler db;

    private Shareprefered mPreferenceSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestAppPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string.msg, REQUEST_PERMISSION);
        requestAppPermission(new String[]{Manifest.permission.CAMERA}, R.string.msg, REQUEST_PERMISSION);

        btnQuiz = (Button) findViewById(R.id.button);
        btnMakeQuiz = (Button) findViewById(R.id.button4);
        btnStatistic = (Button) findViewById(R.id.button3);
        btnProfile = (Button) findViewById(R.id.button2);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        final Context context = this;

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());
        mPreferenceSetting = new Shareprefered(getApplicationContext());
        mPreferenceSetting.mSharedPrefences.edit().clear().apply();

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String email_mak = user.get("email");

        final String email = email_mak;

        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == btnQuiz){
                    Intent i = new Intent(context, ChooseActivity.class);
                    startActivity(i);
                }
            }
        });

        btnMakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == btnMakeQuiz){
                    Intent i = new Intent(context, FormCreateQuizActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                }
            }
        });

        btnStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == btnStatistic){
                    Intent i = new Intent(context, StatisticActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                }
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == btnProfile){
                    Intent i = new Intent(context, profileActivity.class);
                    startActivity(i);
                }
            }
        });

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
          * Logging out the user. Will set isLoggedIn flag to false in shared
          * preferences Clears the user data from sqlite users table
          * */
    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        mPreferenceSetting.mSharedPrefences.edit().clear();
        // Launching the login activity
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        Toast.makeText(getApplicationContext(), "Permission On", Toast.LENGTH_LONG).show();
    }
}
