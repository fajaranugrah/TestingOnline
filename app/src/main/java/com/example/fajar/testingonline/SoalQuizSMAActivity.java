package com.example.fajar.testingonline;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class SoalQuizSMAActivity extends Activity {

    TextView txtNama, txtNo, txtWaktu, txtSoal;
    Button btnPrev, btnSelesai, btnNext;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4;
    ImageView img;
    EditText inputNama;
    int jawabanYgDiPilih[] = null;
    int jawabanYgBenar[] = null;
    boolean cekPertanyaan = false;
    int urutanPertanyaan = 0;
    List<Soal> listSoal;
    JSONArray soal = null;
    CounterClass mCountDownTimer;
    private ProgressDialog pDialog;
    private static String url = "http://aplikasito.hol.es/quiz.php?id_quiz=";
    private static String sent_urlss = "http://aplikasito.hol.es/sent_value.php?judul=";
    private static final String TAG_DAFTAR = "daftar_soal";
    public ImageLoader imageLoader;

    private Shareprefered mPreferenceSetting;
    private SQLiteHandler db;
    private AlertDialog tampilkandialog;

    String jud, email_set;

    long Reminingtimesss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal_quiz_sma);

        listSoal = new ArrayList<Soal>();
        imageLoader = new ImageLoader(getApplicationContext());
        img = (ImageView) findViewById(R.id.imageView1);
        txtNama = (TextView) findViewById(R.id.textViewNama);
        txtNo = (TextView) findViewById(R.id.textViewNo);
        txtWaktu = (TextView) findViewById(R.id.textViewWaktu);
        txtSoal = (TextView) findViewById(R.id.textViewSoal);
        btnPrev = (Button) findViewById(R.id.buttonPrev2);
        btnSelesai = (Button) findViewById(R.id.buttonSelesai2);
        btnNext = (Button) findViewById(R.id.buttonNext2);
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        rb1 = (RadioButton) findViewById(R.id.radio9);
        rb2 = (RadioButton) findViewById(R.id.radio10);
        rb3 = (RadioButton) findViewById(R.id.radio11);
        rb4 = (RadioButton) findViewById(R.id.radio12);


        btnSelesai.setOnClickListener(klikSelesai);
        btnPrev.setOnClickListener(klikSebelum);
        btnNext.setOnClickListener(klikBerikut);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        mPreferenceSetting = new Shareprefered(getApplicationContext());
        tampilkandialog = new AlertDialog.Builder(SoalQuizSMAActivity.this).create();

        Bundle q = new Bundle();
        q = getIntent().getExtras();
        email_set = q.getString("email_ma");
        jud = q.getString("judu");
        Log.d("hjabr :", mPreferenceSetting.getEmail_mas());

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
            pDialog = new ProgressDialog(SoalQuizSMAActivity.this);
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

                        s.setJudul(judul);
                        s.setSoal(soal);
                        s.setPilihan_1(pilihan_1);
                        s.setPilihan_2(pilihan_2);
                        s.setPilihan_3(pilihan_3);
                        s.setPilihan_4(pilihan_4);
                        s.setJawban(jwb);
                        s.setGambar(gambar);
                        listSoal.add(s);

                        mPreferenceSetting.setWaktus(waktu);
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
        if(urutan_soal_soal == 0) {
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

            Log.d("", jawabanYgDiPilih[urutan_soal_soal] + "");
            if (jawabanYgDiPilih[urutan_soal_soal] == 1)
                rg.check(R.id.radio9);
            if (jawabanYgDiPilih[urutan_soal_soal] == 2)
                rg.check(R.id.radio10);
            if (jawabanYgDiPilih[urutan_soal_soal] == 3)
                rg.check(R.id.radio11);
            if (jawabanYgDiPilih[urutan_soal_soal] == 4)
                rg.check(R.id.radio12);

            pasangLabelDanNomorUrut();

            if (urutan_soal_soal == (listSoal.size() - 1)) {
                btnNext.setVisibility(View.INVISIBLE);
                btnSelesai.setEnabled(true);
            }else {
                btnNext.setVisibility(View.VISIBLE);
                btnSelesai.setEnabled(true);
            }

            if (urutan_soal_soal == 0) {
                btnPrev.setVisibility(View.INVISIBLE);
            }else {
                btnPrev.setVisibility(View.VISIBLE);
            }

            if (urutan_soal_soal > 0) {
                btnPrev.setEnabled(true);
                btnSelesai.setEnabled(true);
            }

            if (urutan_soal_soal < (listSoal.size() - 1)) {
                btnNext.setEnabled(true);
            }

        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.getMessage(), e.getCause());
        }
    }

    private OnClickListener klikSelesai = new OnClickListener() {
        public void onClick(View v) {
            aturJawaban_nya();

            final AlertDialog tampilKotakAlert;
            mCountDownTimer.cancel();

            tampilKotakAlert = new AlertDialog.Builder(SoalQuizSMAActivity.this).create();

            tampilKotakAlert.setTitle("Result");
            tampilKotakAlert.setIcon(R.drawable.logo);
            tampilKotakAlert.setMessage("Are you sure ?");

            //agar dialog tidak hilang
            tampilKotakAlert.setCancelable(false);
            tampilKotakAlert.setCanceledOnTouchOutside(false);

            tampilKotakAlert.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){

                    tampilkanScore();

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
            });

            tampilKotakAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    resume();
                }
            });

            tampilKotakAlert.show();


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
            if (urutanPertanyaan >= listSoal.size()){
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
            }else {
                btnPrev.setVisibility(View.VISIBLE);
            }

            tunjukanPertanyaan(urutanPertanyaan, cekPertanyaan);
        }
    };

    private void pasangLabelDanNomorUrut() {
        txtNo.setText("No. " + (urutanPertanyaan + 1)+ " dari "
                + listSoal.size() + " soal");
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
            tampilkanScore();

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
            Reminingtimesss = millisUntilFinished;
        }
    }

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();
        final AlertDialog tampilKotakAlert;
        tampilKotakAlert = new AlertDialog.Builder(SoalQuizSMAActivity.this).create();
        tampilKotakAlert.setTitle("WARNING!!!");
        tampilKotakAlert.setIcon(R.drawable.logo);
        tampilKotakAlert.setMessage("Are you sure want to do this ?");

        //agar dialog tidak hilang
        tampilKotakAlert.setCancelable(false);
        tampilKotakAlert.setCanceledOnTouchOutside(false);

        tampilKotakAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Exit Quiz",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        mCountDownTimer.cancel();
                        finish();
                    }
                });

        tampilKotakAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "Resume",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        resume();
                    }
                });

        tampilKotakAlert.show();
    }

    private void resume(){
        long millisInfuturess = Reminingtimesss;
        long coundownInterval = 1000;
        new CountDownTimer(millisInfuturess, coundownInterval){

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
                tampilkanScore();

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

    private void saveNilai(){
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        final String emails = email;
        int er = Integer.parseInt(mPreferenceSetting.getNilais());

        String email_ans = emails;
        int nilai = er;
        String judul_ans = mPreferenceSetting.getJuduls();
        String email_bu = mPreferenceSetting.getEmail_mas();

        String SENT_URLs = sent_urlss + "&email=" + email_ans + "&nilai=" + nilai + "&judul=" + judul_ans + "&email_mak=" + email_bu;

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.GET, SENT_URLs, new Response.Listener<String>(){
            public void onResponse(String response) {
                Log.d(SoalQuizSMAActivity.class.getSimpleName(), "Save Value Succed: " + response.toString());
            }}, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(SoalQuizSMAActivity.class.getSimpleName(), "Save Value Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        mCountDownTimer.cancel();
    }

    private void tampilkanScore(){
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
}
