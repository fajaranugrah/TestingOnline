package com.example.fajar.testingonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CreateQuestionsActivity extends Activity {

    EditText txtSoal, txtpilihan_1, txtpilihan_2, txtpilihan_3, txtpilihan_4;
    TextView NoView;
    Button next, gambar, takecamera;
    RadioGroup groupjawaban;
    RadioButton jawabanjawaban1, jawabanjawaban2, jawabanjawaban3, jawabanjawaban4;
    ImageView image;
    private static final String TAG = CreateQuestionsActivity.class.getSimpleName();

    private Bitmap bitmap;
    private Uri FilePath;
    final private int PICK_IMAGE_REQUEST = 100;
    final private int CAMERA_IMAGE_REQUEST = 200;
    int urutansoal = 0;

    private Shareprefered mPreferenceSetting;
    private SQLiteHandler db;
    private ProgressDialog pDialogs;
    private CameraPhoto cameraPhoto;

    private String URL = "http://aplikasito.hol.es/create_soal_quiz.php";
    private String KEY_IMAGE = "gambar";
    private String KEY_JUDUL = "judul";
    private String KEY_SOAL = "soal";
    private String KEY_PILIHAN_1 = "pilihan_1";
    private String KEY_PILIHAN_2 = "pilihan_2";
    private String KEY_PILIHAN_3 = "pilihan_3";
    private String KEY_PILIHAN_4 = "pilihan_4";
    private String KEY_JAWABAN = "jawaban";
    private String KEY_EMAIL_MAK = "email_mak";
    private String KEY_ID_QUIZ = "id_quiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_questions);

        NoView = (TextView) findViewById(R.id.text_no_soal);

        txtSoal = (EditText) findViewById(R.id.input_soal);
        txtpilihan_1 = (EditText) findViewById(R.id.jawaban1);
        txtpilihan_2 = (EditText) findViewById(R.id.jawaban2);
        txtpilihan_3 = (EditText) findViewById(R.id.jawaban3);
        txtpilihan_4 = (EditText) findViewById(R.id.jawaban4);

        groupjawaban = (RadioGroup) findViewById(R.id.table);
        jawabanjawaban1 = (RadioButton) findViewById(R.id.jawaban_benar_1);
        jawabanjawaban2 = (RadioButton) findViewById(R.id.jawaban_benar_2);
        jawabanjawaban3 = (RadioButton) findViewById(R.id.jawaban_benar_3);
        jawabanjawaban4 = (RadioButton) findViewById(R.id.jawaban_benar_4);

        next = (Button) findViewById(R.id.button_next);
        gambar = (Button) findViewById(R.id.buttonchoice);
        takecamera = (Button) findViewById(R.id.buttoncamera);

        image = (ImageView) findViewById(R.id.imag);

        image.setImageBitmap(null);
        next.setOnClickListener(nex);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        mPreferenceSetting = new Shareprefered(getApplicationContext());
        cameraPhoto = new CameraPhoto(getApplicationContext());
        // Progress dialog
        pDialogs = new ProgressDialog(this);
        pDialogs.setCancelable(false);

        gambar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choosegambar();
            }
        });

        takecamera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                takegambar();
            }
        });

        int p = Integer.parseInt(mPreferenceSetting.getBanyaksoal());
        Log.d("adwaq :", String.valueOf(p));

        bitmap = null;

    }

    private void takegambar(){
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_IMAGE_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cameraPhoto.addToGallery();
    }

    private void choosegambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                FilePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePath);
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_IMAGE_REQUEST) {
                String photo = cameraPhoto.getPhotoPath();
                try {
                    bitmap = ImageLoader.init().from(photo).getBitmap();
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private OnClickListener nex = new OnClickListener() {
        public void onClick(View v) {
            int p = Integer.parseInt(mPreferenceSetting.getBanyaksoal());

            if (bitmap==null){
                //Showing toast
                Toast.makeText(getApplicationContext(), "No Images", Toast.LENGTH_LONG).show();
            }else {
                //Showing toast
                Toast.makeText(getApplicationContext(), "There is a Images ", Toast.LENGTH_LONG).show();
            }

            String imag = null;
            //Converting Bitmap to String
            if (bitmap==null){
                String ima = String.valueOf(R.drawable.logo);
                imag = ima;
            }else{
                String ima = getStringImage(bitmap);
                imag = ima;
            }
            final String Image = imag;

            //Getting Input Data
            HashMap<String, String> user = db.getUserDetails();
            String email = user.get("email");
            final String emails = email;
            String email_ans = emails;

            String soal = txtSoal.getText().toString().trim();
            String judulsoal = mPreferenceSetting.getSoalJudul();
            String Jawaban1 = txtpilihan_1.getText().toString().trim();
            String Jawaban2 = txtpilihan_2.getText().toString().trim();
            String Jawaban3 = txtpilihan_3.getText().toString().trim();
            String Jawaban4 = txtpilihan_4.getText().toString().trim();
            Log.d("qwasdwa ", soal);

            String jawaban0 = null;
            if (jawabanjawaban1.isChecked()) {
                int jawabans = 1;
                jawaban0 = String.valueOf(jawabans);
            }
            if (jawabanjawaban2.isChecked()) {
                int jawabans = 2;
                jawaban0 = String.valueOf(jawabans);
            }
            if (jawabanjawaban3.isChecked()) {
                int jawabans = 3;
                jawaban0 = String.valueOf(jawabans);
            }
            if (jawabanjawaban4.isChecked()) {
                int jawabans = 4;
                jawaban0 = String.valueOf(jawabans);
            }
            final String jaw = jawaban0;
            Log.d("qweqpoad :", jaw);

            String id_quiz = mPreferenceSetting.getId_quiz();
            Log.d("pdpaldp", id_quiz);

            urutansoal++;
            //for (int i=0; i<p; i++){
            if (urutansoal == p) {
                urutanQuestionlast(Image, email_ans, soal, judulsoal, Jawaban1, Jawaban2, Jawaban3, Jawaban4, jaw, id_quiz);
            } else if (urutansoal < p){
                urutanQuestion(Image, email_ans, soal, judulsoal, Jawaban1, Jawaban2, Jawaban3, Jawaban4, jaw, id_quiz);

                txtSoal.getText().clear();
                txtpilihan_1.getText().clear();
                txtpilihan_2.getText().clear();
                txtpilihan_3.getText().clear();
                txtpilihan_4.getText().clear();
                groupjawaban.clearCheck();
                image.setImageResource(0);
                bitmap = null;

            }
            //}
        }
    };

    private void urutanQuestion(final String Image, final String email_ans, final String soal, final String judulsoal, final String Jawaban1, final String Jawaban2, final String Jawaban3, final String Jawaban4, final String jaw, final String id_quiz) {

        final AlertDialog tampilkandialog;
        tampilkandialog = new AlertDialog.Builder(CreateQuestionsActivity.this).create();

        tampilkandialog.setTitle("WARNING!!!");
        tampilkandialog.setMessage("Are you sure with questions you do ? ");

        tampilkandialog.setCancelable(false);
        tampilkandialog.setCanceledOnTouchOutside(false);

        tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Change Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next Questions",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        uploadImage(Image, email_ans, soal, judulsoal, Jawaban1, Jawaban2, Jawaban3, Jawaban4, jaw, id_quiz);
                    }
                });

        tampilkandialog.show();
    }

    public void urutanQuestionlast(final String Image, final String email_ans, final String soal, final String judulsoal, final String Jawaban1, final String Jawaban2, final String Jawaban3, final String Jawaban4, final String jaw, final String id_quiz) {
        final AlertDialog tampilkandialog;
        tampilkandialog = new AlertDialog.Builder(CreateQuestionsActivity.this).create();

        tampilkandialog.setTitle("WARNING!!!");
        tampilkandialog.setMessage("Are you sure with questions you do ? This is the Last");

        tampilkandialog.setCancelable(false);
        tampilkandialog.setCanceledOnTouchOutside(false);

        tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Change Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "Finished make",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        uploadImage(Image, email_ans, soal, judulsoal, Jawaban1, Jawaban2, Jawaban3, Jawaban4, jaw, id_quiz);

                        Intent in = new Intent(CreateQuestionsActivity.this, MainActivity.class);
                        startActivity(in);
                        //finish();
                    }
                });

        tampilkandialog.show();
    }

    private void uploadImage(final String Image, final String email_ans, final String soal, final String judulsoal, final String Jawaban1, final String Jawaban2, final String Jawaban3, final String Jawaban4, final String jaw, final String id_quiz) {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_JUDUL, judulsoal);
                params.put(KEY_SOAL, soal);
                params.put(KEY_PILIHAN_1, Jawaban1);
                params.put(KEY_PILIHAN_2, Jawaban2);
                params.put(KEY_PILIHAN_3, Jawaban3);
                params.put(KEY_PILIHAN_4, Jawaban4);
                params.put(KEY_JAWABAN, jaw);
                params.put(KEY_IMAGE, Image);
                params.put(KEY_EMAIL_MAK, email_ans);
                params.put(KEY_ID_QUIZ, id_quiz);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog tampilKotakAlert;
        tampilKotakAlert = new AlertDialog.Builder(CreateQuestionsActivity.this).create();
        tampilKotakAlert.setTitle("WARNING!!!");
        tampilKotakAlert.setIcon(R.drawable.logo);
        tampilKotakAlert.setMessage("Are you sure want to do this ?");

        tampilKotakAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Exit Quiz",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        tampilKotakAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "Resume",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

    }
}
