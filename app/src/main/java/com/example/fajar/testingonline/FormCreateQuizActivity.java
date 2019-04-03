package com.example.fajar.testingonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormCreateQuizActivity extends Activity implements OnItemSelectedListener{

    private static final String TAG = FormCreateQuizActivity.class.getSimpleName();
    private EditText txtjudul, judul_level;
    private EditText txtbanyaksoal;
    private EditText jam;
    private EditText menit;
    private EditText detik;
    private Button buat;
    private CheckBox needscore;
    //private CheckBox pilih_level;
    Spinner kategori;
    private ProgressDialog pDialogs;

    private static String urls = "http://aplikasito.hol.es/create_title.php?judul=";

    private Shareprefered mPreferenceSetting;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_create_quiz);

        txtjudul = (EditText) findViewById(R.id.judul);
        txtbanyaksoal = (EditText) findViewById(R.id.banyak_soal);
        jam = (EditText) findViewById(R.id.jam);
        menit = (EditText) findViewById(R.id.menit);
        detik = (EditText) findViewById(R.id.detik);
        buat = (Button) findViewById(R.id.buat_soal);
        kategori = (Spinner) findViewById(R.id.kategori);
        needscore = (CheckBox) findViewById(R.id.NeedScore);
        //judul_level = (EditText) findViewById(R.id.level);
        //pilih_level = (CheckBox) findViewById(R.id.pilih_level);

        // Progress dialog
        pDialogs = new ProgressDialog(this);
        pDialogs.setCancelable(false);

        /*if (pilih_level.isChecked()){
            judul_level.setVisibility(View.GONE);
        }else {
            judul_level.setVisibility(View.VISIBLE);
        }*/

        kategori.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Elementary School");
        categories.add("Junior High School");
        categories.add("Senior High School");
        categories.add("General");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        kategori.setAdapter(dataAdapter);

        mPreferenceSetting = new Shareprefered(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        String pilihan = item;
        int pilihank;
        String pilihanin = null;

        if(pilihan == "Elementary School"){
            pilihank = 1;
            pilihanin = String.valueOf(pilihank);
            Log.d("lkjh :", String.valueOf(pilihank));
            //mPreferenceSetting.setPilihan_Kategori(String.valueOf(pilihank));
        }if(pilihan == "Junior High School"){
            pilihank = 2;
            pilihanin = String.valueOf(pilihank);
            Log.d("lkjh :", String.valueOf(pilihank));
            //mPreferenceSetting.setPilihan_Kategori(String.valueOf(pilihank));
        }if (pilihan == "Senior High School"){
            pilihank = 3;
            pilihanin = String.valueOf(pilihank);
            Log.d("lkjh :", String.valueOf(pilihank));
            //mPreferenceSetting.setPilihan_Kategori(String.valueOf(pilihank));
        }if(pilihan == "General"){
            pilihank = 4;
            pilihanin = String.valueOf(pilihank);
            Log.d("lkjh :", String.valueOf(pilihank));
            //mPreferenceSetting.setPilihan_Kategori(String.valueOf(pilihank));
        }
        //mPreferenceSetting.setPilihan_Kategori(pilihanin);
        final String pili = pilihanin;
        Log.d("mcnd :", pili);

        int score;
        String Score = null;
        String NamScore = null;

        if (needscore.isChecked()){
            score = 1;
            Score = String.valueOf(score);
            needscore.setChecked(true);
            NamScore = "Yes";
        }else {
            score = 2;
            Score = String.valueOf(score);
            needscore.setChecked(false);
            NamScore = "No";
        }
        final String NdScore = Score;
        final String TestScore = NamScore;

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        Toast.makeText(parent.getContext(), "You Need score: " + TestScore, Toast.LENGTH_LONG).show();

        buat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String jams = jam.getText().toString().trim();
                String jamss = menit.getText().toString();
                String jamsss = detik.getText().toString();
                String Judul = txtjudul.getText().toString().replace(" ", "_");
                //String level = judul_level.getText().toString().trim();
                //String Judul = Judulss + " " + level;
                String Banyaksoal = txtbanyaksoal.getText().toString().trim();
                Log.d("wewewe", txtjudul.getText().toString().trim());
                String kategori1 = pili;
                String NeedScore = NdScore;
                Log.d("zxcv :", Banyaksoal);

                if (!Judul.isEmpty() && !Banyaksoal.isEmpty() && !jams.isEmpty() && !jamss.isEmpty() && !jamsss.isEmpty()) {
                    Judul_soal(Judul, Banyaksoal, jams, jamss, jamsss, kategori1);
                } else {
                    AlertDialog tampilKotakAlert;
                    tampilKotakAlert = new AlertDialog.Builder(FormCreateQuizActivity.this).create();
                    tampilKotakAlert.setMessage("Please full the Form your details Questions!");
                    tampilKotakAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    tampilKotakAlert.show();
                    //Toast.makeText(getApplicationContext(),"Please full the Form your details Questions!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void Judul_soal(final String Judul, final String Banyaksoal, final String jams, final String jamss, final String jamsss, final String kategori1){

        int hourss = Integer.parseInt(jams);
        int minutes = Integer.parseInt(jamss);
        int seconds = Integer.parseInt(jamsss);

        int waktus = hourss * 3600;
        int waktuss = minutes * 60;
        int waktusss = seconds * 1;

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        final String email_mak = user.get("email");
        final String id_mysql = user.get("id_mysql");

        int time = waktus + waktuss + waktusss;
        final String waktu = String.valueOf(time);
        Log.d("dfgh :", waktu);
        Log.d("ldksal :", Judul);

        String SENTs_URL = urls + Judul + "&banyak_soal=" + Banyaksoal + "&id=" + id_mysql + "&email_mak=" + email_mak + "&waktu=" + waktu + "&kategori=" + kategori1;

        mPreferenceSetting.setBanyaksoal(Banyaksoal);
        Log.d("pqowe:", mPreferenceSetting.getBanyaksoal());
        mPreferenceSetting.setSoalJudul(Judul);


        pDialogs.setMessage("Registering Title ...");
        showDialog();

        // Tag used to cancel the request
        String tag_string_reqs = "req_register";

        StringRequest strReqss = new StringRequest(Method.GET, SENTs_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        String id_quiz = user.getString("id_quiz");

                        mPreferenceSetting.setId_quiz(id_quiz);
                        Log.d("lkjhg", mPreferenceSetting.getId_quiz());
                        // Launch main activity
                        Intent in = new Intent(FormCreateQuizActivity.this, CreateQuestionsActivity.class);
                        startActivity(in);
                        finish();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        String errorg = errorMsg.toString().replace("_"," ");

                        AlertDialog tampilKotakAlert;
                        tampilKotakAlert = new AlertDialog.Builder(FormCreateQuizActivity.this).create();

                        tampilKotakAlert.setTitle("Change Your Title");
                        tampilKotakAlert.setMessage("Sorry, Title already existed with " + errorg);

                        tampilKotakAlert.setCancelable(false);
                        tampilKotakAlert.setCanceledOnTouchOutside(false);

                        tampilKotakAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                        });
                        //Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                        tampilKotakAlert.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Title Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReqss, tag_string_reqs);
    }

    private void showDialog() {
        if (!pDialogs.isShowing())
            pDialogs.show();
    }

    private void hideDialog() {
        if (pDialogs.isShowing())
            pDialogs.dismiss();
    }
}
