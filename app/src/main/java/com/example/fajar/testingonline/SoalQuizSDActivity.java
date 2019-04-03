package com.example.fajar.testingonline;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SoalQuizSDActivity extends Activity{

    TextView txtNama, txtNo, txtWaktu, txtSoal;
    Button btnPrev, btnSelesai, btnNext, btnselesaiEdit;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4;
    ImageButton sumber, editJawaban;
    ImageView img;
    int jawabanYgDiPilih[] = null;
    int jawabanYgBenar[] = null;
    boolean cekPertanyaan = false;
    boolean review = false;
    int urutanPertanyaan = 0;
    List<Soal> listSoal;
    JSONArray soal = null;
    CounterClass mCountDownTimer;
    private ProgressDialog pDialog;
    private static String url = "http://aplikasito.hol.es/quiz.php?id_quiz=";
    private static String sent_urls = "http://aplikasito.hol.es/sent_value.php?judul=";
    private static final String TAG_DAFTAR = "daftar_soal";
    public ImageLoader imageLoader;

    private Shareprefered mPreferenceSetting;
    private SQLiteHandler db;
    private AlertDialog tampilkandialog;

    String jud, email_set;

    long Reminingtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal_quiz_sd);
        initView();
    }

    private void initView(){
        listSoal = new ArrayList<Soal>();
        imageLoader = new ImageLoader(getApplicationContext());
        img = (ImageView) findViewById(R.id.imageView1);
        txtNama = (TextView) findViewById(R.id.textViewNama);
        txtNo = (TextView) findViewById(R.id.textViewNo);
        txtWaktu = (TextView) findViewById(R.id.textViewWaktu);
        txtSoal = (TextView) findViewById(R.id.textViewSoal);
        btnPrev = (Button) findViewById(R.id.buttonPrev);
        btnSelesai = (Button) findViewById(R.id.buttonSelesai);
        btnNext = (Button) findViewById(R.id.buttonNext);
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        rb1 = (RadioButton) findViewById(R.id.radio0);
        rb2 = (RadioButton) findViewById(R.id.radio1);
        rb3 = (RadioButton) findViewById(R.id.radio2);
        rb4 = (RadioButton) findViewById(R.id.radio3);

        sumber = (ImageButton) findViewById(R.id.sumber);
        editJawaban = (ImageButton) findViewById(R.id.editsoal);
        btnselesaiEdit = (Button)findViewById(R.id.buttonselesaiedit);

        btnSelesai.setOnClickListener(klikSelesai);
        btnPrev.setOnClickListener(klikSebelum);
        btnNext.setOnClickListener(klikBerikut);
        sumber.setOnClickListener(klikSumber);
        editJawaban.setOnClickListener(klikEditJawaban);
        btnselesaiEdit.setOnClickListener(klikSelesaiEdit);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        mPreferenceSetting = new Shareprefered(getApplicationContext());
        tampilkandialog = new AlertDialog.Builder(SoalQuizSDActivity.this).create();

        Bundle q = new Bundle();
        q = getIntent().getExtras();
        email_set = q.getString("email_ma");
        jud = q.getString("judu");
        Log.d("hjabr :", mPreferenceSetting.getId_quiz());

        showInputUser();
    }


    public void showInputUser() {
        new GetSoal().execute();

        HashMap<String, String> user = db.getUserDetails();
        String nama = user.get("name");

        final String name = nama.toString().replace("_"," ");
        txtNama.setText(name);
    }


    public class GetSoal extends AsyncTask<Void, Void, Void> {

        String judu = mPreferenceSetting.getJuduls();
        String email_ma = mPreferenceSetting.getEmail_mas();
        String id_quiz = mPreferenceSetting.getId_quiz();

        String URL = url + id_quiz + "&email_mak=" + email_ma;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SoalQuizSDActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    soal = jsonObj.getJSONArray(TAG_DAFTAR);
                    Soal s = null;
                    // looping through All Contacts
                    for (int i = 0; i < soal.length(); i++) {
                        JSONObject c = soal.getJSONObject(i);
                        s = new Soal();

                        String judul = c.getString("judul");
                        String soal = c.getString("soal");
                        String pilihan_1 = c.getString("pilihan_1");
                        String pilihan_2 = c.getString("pilihan_2");
                        String pilihan_3 = c.getString("pilihan_3");
                        String pilihan_4 = c.getString("pilihan_4");
                        String waktu = c.getString("waktu");
                        String banyak_soal = c.getString("banyak_soal");
                        String jwb = c.getString("jawaban");
                        String gambar = c.getString("gambar");
                        String sumber = c.getString("sumber");
                        String id = c.getString("id");
                        String imagerjk = c.getString("imagesrc");

                        s.setJudul(judul);
                        s.setSoal(soal);
                        s.setPilihan_1(pilihan_1);
                        s.setPilihan_2(pilihan_2);
                        s.setPilihan_3(pilihan_3);
                        s.setPilihan_4(pilihan_4);
                        s.setJawban(jwb);
                        s.setGambar(gambar);
                        s.setSumber(sumber);
                        s.setId(id);
                        s.setImageRjk(imagerjk);
                        listSoal.add(s);

                        mPreferenceSetting.setWaktus(waktu);
                        Log.d("dpqowei", mPreferenceSetting.getWaktus());
                        mPreferenceSetting.setBanyak_Soal(banyak_soal);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            jawabanYgDiPilih = new int[listSoal.size()];
            java.util.Arrays.fill(jawabanYgDiPilih, -1);
            jawabanYgBenar = new int[listSoal.size()];
            java.util.Arrays.fill(jawabanYgBenar, -1);
            setUpSoal();
        }
    }

    private void setUpSoal() {
        Collections.shuffle(listSoal);
        tunjukanPertanyaan(0, cekPertanyaan);
    }

    private void tunjukanPertanyaan(int urutan_soal_soal, boolean review) {
        btnSelesai.setEnabled(false);
        //sumber.setVisibility(View.INVISIBLE);
        if(urutan_soal_soal == 0){
            setUpWaktu();
        }
        try {
            Soal soal = new Soal();
            rg.clearCheck();
            soal = listSoal.get(urutan_soal_soal);
            if (jawabanYgBenar[urutan_soal_soal] == -1) {
                jawabanYgBenar[urutan_soal_soal] = Integer.parseInt(soal.getJawban());
            }

            String soalnya = soal.getSoal();
            txtSoal.setText(soalnya);
            rg.check(-1);
            rb1.setTextColor(Color.BLACK);
            rb2.setTextColor(Color.BLACK);
            rb3.setTextColor(Color.BLACK);
            rb4.setTextColor(Color.BLACK);
            imageLoader.DisplayImage(soal.getGambar(), img);
            rb1.setText(soal.getPilihan_1());
            rb2.setText(soal.getPilihan_2());
            rb3.setText(soal.getPilihan_3());
            rb4.setText(soal.getPilihan_4());
            mPreferenceSetting.setSumber(soal.getSumber());
            mPreferenceSetting.setImageRjk(soal.getImageRjk());
            mPreferenceSetting.setId(soal.getId());

            Log.d("", jawabanYgDiPilih[urutan_soal_soal] + "");
            if (jawabanYgDiPilih[urutan_soal_soal] == 1)
                rg.check(R.id.radio0);
            if (jawabanYgDiPilih[urutan_soal_soal] == 2)
                rg.check(R.id.radio1);
            if (jawabanYgDiPilih[urutan_soal_soal] == 3)
                rg.check(R.id.radio2);
            if (jawabanYgDiPilih[urutan_soal_soal] == 4)
                rg.check(R.id.radio3);

            pasangLabelDanNomorUrut();

            if (urutan_soal_soal == (listSoal.size() - 1)) {
                btnNext.setVisibility(View.INVISIBLE);
                btnSelesai.setEnabled(true);
                sumber.setVisibility(View.INVISIBLE);
                editJawaban.setVisibility(View.INVISIBLE);
                btnselesaiEdit.setVisibility(View.INVISIBLE);
            }else {
                btnNext.setVisibility(View.VISIBLE);
                btnSelesai.setEnabled(true);
                sumber.setVisibility(View.INVISIBLE);
                editJawaban.setVisibility(View.INVISIBLE);
                btnselesaiEdit.setVisibility(View.INVISIBLE);
            }

            if (urutan_soal_soal == 0) {
                btnPrev.setVisibility(View.INVISIBLE);
                sumber.setVisibility(View.INVISIBLE);
                editJawaban.setVisibility(View.INVISIBLE);
                btnselesaiEdit.setVisibility(View.INVISIBLE);
            }else {
                btnPrev.setVisibility(View.VISIBLE);
                sumber.setVisibility(View.INVISIBLE);
                editJawaban.setVisibility(View.INVISIBLE);
                btnselesaiEdit.setVisibility(View.INVISIBLE);
            }

            if (urutan_soal_soal > 0) {
                btnPrev.setEnabled(true);
                btnSelesai.setEnabled(true);
                sumber.setVisibility(View.INVISIBLE);
                editJawaban.setVisibility(View.INVISIBLE);
                btnselesaiEdit.setVisibility(View.INVISIBLE);
            }

            if (urutan_soal_soal < (listSoal.size() - 1)) {
                btnNext.setEnabled(true);
                sumber.setVisibility(View.INVISIBLE);
                editJawaban.setVisibility(View.INVISIBLE);
                btnselesaiEdit.setVisibility(View.INVISIBLE);
            }

            if (review) {
                mCountDownTimer.cancel();
                sumber.setVisibility(View.VISIBLE);
                editJawaban.setVisibility(View.VISIBLE);
                btnSelesai.setVisibility(View.INVISIBLE);
                btnselesaiEdit.setVisibility(View.VISIBLE);
                Log.d("priksa", jawabanYgDiPilih[urutan_soal_soal] + ""
                        + jawabanYgBenar[urutan_soal_soal]);
                if (jawabanYgDiPilih[urutan_soal_soal] != jawabanYgBenar[urutan_soal_soal]) {
                    if (jawabanYgDiPilih[urutan_soal_soal] == 1)
                        rb1.setTextColor(Color.RED);
                    if (jawabanYgDiPilih[urutan_soal_soal] == 2)
                        rb2.setTextColor(Color.RED);
                    if (jawabanYgDiPilih[urutan_soal_soal] == 3)
                        rb3.setTextColor(Color.RED);
                    if (jawabanYgDiPilih[urutan_soal_soal] == 4)
                        rb3.setTextColor(Color.RED);
                }
                if (jawabanYgBenar[urutan_soal_soal] == 1)
                    rb1.setTextColor(Color.GREEN);
                if (jawabanYgBenar[urutan_soal_soal] == 2)
                    rb2.setTextColor(Color.GREEN);
                if (jawabanYgBenar[urutan_soal_soal] == 3)
                    rb3.setTextColor(Color.GREEN);
                if (jawabanYgBenar[urutan_soal_soal] == 4)
                    rb3.setTextColor(Color.GREEN);
            }

        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e.getCause());
        }
    }

    private OnClickListener klikSelesai = new OnClickListener() {
        public void onClick(View v) {
            aturJawaban_nya();

            tampilnotif();
        }
    };

    private void aturJawaban_nya() {
        if (rb1.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 1;
        if (rb2.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 2;
        if (rb3.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 3;
        if (rb4.isChecked())
            jawabanYgDiPilih[urutanPertanyaan] = 4;

        Log.d("", Arrays.toString(jawabanYgDiPilih));
        Log.d("", Arrays.toString(jawabanYgBenar));

    }

    private OnClickListener klikBerikut = new OnClickListener() {
        public void onClick(View v) {
            aturJawaban_nya();
            urutanPertanyaan++;
            if (urutanPertanyaan >= listSoal.size()) {
                urutanPertanyaan = listSoal.size() - 1;
                btnNext.setVisibility(View.INVISIBLE);
            }else {
                btnNext.setVisibility(View.VISIBLE);
            }
            tunjukanPertanyaan(urutanPertanyaan, cekPertanyaan);
        }
    };

    private OnClickListener klikSebelum = new OnClickListener() {
        public void onClick(View v) {
            aturJawaban_nya();
            urutanPertanyaan--;
            if (urutanPertanyaan < 0) {
                urutanPertanyaan = 0;
                btnPrev.setVisibility(View.INVISIBLE);
            }else{
                btnPrev.setVisibility(View.VISIBLE);
            }
            tunjukanPertanyaan(urutanPertanyaan, cekPertanyaan);
        }
    };

    private OnClickListener klikSumber = new OnClickListener() {
        @Override
        public void onClick(View view) {
            final String link = mPreferenceSetting.getSumber();
            final String gambarSumber = mPreferenceSetting.getImageRjk();
            tampilkandialog.setTitle("Source");
            tampilkandialog.setIcon(R.drawable.logo);
            tampilkandialog.setMessage("Edited By : " + mPreferenceSetting.getEmail_mas() + "\n\n" + "Link :" + link + "\n\n"+ "Image :"+gambarSumber);

            //agar dialog tidak hilang
            tampilkandialog.setCancelable(false);
            tampilkandialog.setCanceledOnTouchOutside(false);

            tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View Web",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SoalQuizSDActivity.this, SeeRujukan.class);
                            intent.putExtra("link", link);
                            startActivity(intent);
                        }
                    });
            tampilkandialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View Image",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SoalQuizSDActivity.this, SeeImage.class);
                            intent.putExtra("imageLink", gambarSumber);
                            startActivity(intent);
                        }
                    });

            tampilkandialog.show();
        }
    };
    private OnClickListener klikSelesaiEdit = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SoalQuizSDActivity.this, QuizSDActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private OnClickListener klikEditJawaban = new OnClickListener() {
        @Override
        public void onClick(View view) {
            final String id = mPreferenceSetting.getId();
            tampilkandialog.setTitle("Edit Quiz");
            tampilkandialog.setIcon(R.drawable.logo);
            tampilkandialog.setMessage("Are you sure ?");

            //agar dialog tidak hilang
            tampilkandialog.setCancelable(false);
            tampilkandialog.setCanceledOnTouchOutside(false);

            tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            tampilkandialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SoalQuizSDActivity.this, EditQuizActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    });

            tampilkandialog.show();
        }
    };

    private void pasangLabelDanNomorUrut() {
        txtNo.setText("No. " + (urutanPertanyaan + 1)+ " of "
                + listSoal.size() + " Questions");
    }

    private void setUpWaktu() {
        int aw = Integer.parseInt(mPreferenceSetting.getWaktus());
        int a = 1000;
        int w = aw * a;
        mCountDownTimer = new CounterClass(w, 1000);
        mCountDownTimer.start();
    }

    @SuppressLint("DefaultLocale")
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            //if (needscore==1){
                tampilScore();
            //}else if (needscore==2){
                //tidaktampilScore();
            //}

            tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            saveNilai();
                            finish();
                        }
                    });

            tampilkandialog.show();
        }

        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            txtWaktu.setText(hms);
            Reminingtime = millisUntilFinished;
        }
    }

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();

        tampilkandialog.setTitle("WARNING!!!");
        tampilkandialog.setIcon(R.drawable.logo);
        tampilkandialog.setMessage("Are you sure want to do this ?");

        //agar dialog tidak hilang
        tampilkandialog.setCancelable(false);
        tampilkandialog.setCanceledOnTouchOutside(false);

        tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "Exit Quiz",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer.cancel();
                        finish();
                    }
                });

        tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Resume",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        resume();
                    }
                });

        tampilkandialog.show();
    }

    private void resume(){
        long millisInfutures = Reminingtime;
        long coundownInterval = 1000;
        new CountDownTimer(millisInfutures, coundownInterval){

            public void onTick(long millisUntilFinish) {
                long millis = millisUntilFinish;
                String hms = String.format(
                        "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txtWaktu.setText(hms);
            };

            public void onFinish(){

                //if (needscore==1){
                    tampilScore();
                //}else if (needscore==2){
                    //tidaktampilScore();
                //}


                tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                saveNilai();
                                finish();
                            }
                        });

                tampilkandialog.show();
            }
        }.start();
    }

    private void tampilnotif(){
        mCountDownTimer.cancel();

        tampilkandialog.setTitle("Result");
        tampilkandialog.setIcon(R.drawable.logo);
        tampilkandialog.setMessage("Are you sure ?");

        //agar dialog tidak hilang
        tampilkandialog.setCancelable(false);
        tampilkandialog.setCanceledOnTouchOutside(false);

        tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

                //if (needscore==1){
                    tampilScore();
                    pilihanOptionScore();
                //}else if (needscore==2){
                    //tidaktampilScore();
                    pilihanOptionTidakScore();
                //}

            }
        });

        tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resume();
            }
        });

        tampilkandialog.show();
    }

    private void pilihanOptionTidakScore(){
        tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Play Again",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer.cancel();
                        java.util.Arrays.fill(jawabanYgDiPilih, -1);
                        cekPertanyaan = false;
                        urutanPertanyaan = 0;
                        tunjukanPertanyaan(0, cekPertanyaan);
                    }
                });

        tampilkandialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Check",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer.cancel();
                        cekPertanyaan = true;
                        urutanPertanyaan = 0;
                        tunjukanPertanyaan(0, cekPertanyaan);
                    }
                });

        tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer.cancel();
                        //cekPertanyaan = true;
                        //urutanPertanyaan = 0;
                        //tunjukanPertanyaan(0, cekPertanyaan);

                        saveNilai();
                        finish();
                    }
                });
        tampilkandialog.show();
    }

    private void pilihanOptionScore(){
        tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Play Again",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer.cancel();
                        java.util.Arrays.fill(jawabanYgDiPilih, -1);
                        cekPertanyaan = false;
                        urutanPertanyaan = 0;
                        tunjukanPertanyaan(0, cekPertanyaan);
                    }
                });

        tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer.cancel();
                        //cekPertanyaan = true;
                        //urutanPertanyaan = 0;
                        //tunjukanPertanyaan(0, cekPertanyaan);

                        saveNilai();
                        finish();
                    }
                });
        tampilkandialog.show();
    }

    private void saveNilai(){
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        final String emails = email;
        int er = Integer.parseInt(mPreferenceSetting.getNilais());

        String email_ans = emails;
        int nilai = er;
        String judul_ans = mPreferenceSetting.getJuduls();
        String email_bu = mPreferenceSetting.getEmail_mas();

        String SENT_URLs = sent_urls + "&email=" + email_ans + "&nilai=" + nilai + "&judul=" + judul_ans + "&email_mak=" + email_bu;

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.GET, SENT_URLs, new Response.Listener<String>(){
            public void onResponse(String response) {
                Log.d(SoalQuizSDActivity.class.getSimpleName(), "Save Value Succed: " + response.toString());
            }}, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(SoalQuizSDActivity.class.getSimpleName(), "Save Value Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        mCountDownTimer.cancel();
    }

    private void tampilScore(){
        int jumlahJawabanYgBenar = 0;
        for (int i = 0; i < jawabanYgBenar.length; i++) {
            if ((jawabanYgBenar[i] != -1) && (jawabanYgBenar[i] == jawabanYgDiPilih[i]))jumlahJawabanYgBenar++;
        }

        int so = Integer.parseInt(mPreferenceSetting.getBanyak_Soal());
        Log.d("pwepwe", mPreferenceSetting.getBanyak_Soal());
        mCountDownTimer.cancel();

        int hs = 100/so;
        int io = jumlahJawabanYgBenar * hs;
        tampilkandialog.setTitle("Result");
        tampilkandialog.setIcon(R.drawable.logo);
        tampilkandialog.setMessage("Score " + io);
        String po = String.valueOf(io);
        mPreferenceSetting.setNilais(po);

        //agar dialog tidak hilang
        tampilkandialog.setCancelable(false);
        tampilkandialog.setCanceledOnTouchOutside(false);
    }

    private void tidaktampilScore(){
        mCountDownTimer.cancel();

        tampilkandialog.setTitle("Thank you");
        tampilkandialog.setIcon(R.drawable.logo);
        tampilkandialog.setMessage("For The Answer to the Question that I Made");

        //agar dialog tidak hilang
        tampilkandialog.setCancelable(false);
        tampilkandialog.setCanceledOnTouchOutside(false);
    }
}
