package com.example.fajar.testingonline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashMap;

public class profileActivity extends AppCompatActivity {

    private TextView txtName;
    private TextView txtEmail;
    //private Button btnLogout;

    private SQLiteHandler db;
    //private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        //btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        //session = new SessionManager(getApplicationContext());

        /*if (!session.isLoggedIn()) {
            logoutUser();
            }
*/
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        String nam = name.toString().replace("_", " ");
        txtName.setText(nam);
        txtEmail.setText(email);

        /*// Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }*/
    }

            /**
          * Logging out the user. Will set isLoggedIn flag to false in shared
          * preferences Clears the user data from sqlite users table
          * *//*
            private void logoutUser() {
                session.setLogin(false);
                db.deleteUsers();
                // Launching the login activity
                Intent i = new Intent(profileActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }*/
}