package com.example.fajar.testingonline;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.CameraPhoto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EditQuizActivity extends AppCompatActivity {

    private Button viewLink;
    private Button cameraLink;
    private Button fileLink;
    private Button saveEdit;
    private EditText urlLink;
    private EditText txtPilihan1Edit1, txtPilihan1Edit2, txtPilihan1Edit3, txtPilihan1Edit4;
    private RadioGroup tableEdit;
    private RadioButton jawabanEdit1, jawabanEdit2, jawabanEdit3, jawabanEdit4;
    private ImageView imageLink;

    private Shareprefered mPreferenceSetting;
    private SQLiteHandler db;
    private ProgressDialog pDialogs;
    private CameraPhoto cameraPhoto;

    private String URL = "http://aplikasito.hol.es/edit_quiz.php";
    private String KEY_IMAGE = "imagesrc";
    private String KEY_LINK = "sumber";
    private String KEY_PILIHAN_1 = "pilihan_1";
    private String KEY_PILIHAN_2 = "pilihan_2";
    private String KEY_PILIHAN_3 = "pilihan_3";
    private String KEY_PILIHAN_4 = "pilihan_4";
    private String KEY_JAWABAN = "jawaban";
    private String KEY_EMAIL_MAK = "edit_email";
    private String KEY_ID = "id";

    private Bitmap bitmap;
    private Uri FilePath;
    final private int PICK_IMAGE_REQUEST = 300;
    final private int CAMERA_IMAGE_REQUEST = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        initView();
    }

    private void initView(){
        viewLink = (Button)findViewById(R.id.viewLink);
        cameraLink = (Button)findViewById(R.id.buttonCamera);
        fileLink = (Button)findViewById(R.id.buttonchoiceFile);
        saveEdit = (Button)findViewById(R.id.buttonSave);
        urlLink = (EditText)findViewById(R.id.link);
        txtPilihan1Edit1 = (EditText)findViewById(R.id.editJawaban1);
        txtPilihan1Edit2 = (EditText)findViewById(R.id.editJawaban2);
        txtPilihan1Edit3 = (EditText)findViewById(R.id.editJawaban3);
        txtPilihan1Edit3 = (EditText)findViewById(R.id.editJawaban4);

        tableEdit = (RadioGroup)findViewById(R.id.tableEdit);
        jawabanEdit1 = (RadioButton)findViewById(R.id.edit_jawaban_benar_1);
        jawabanEdit2 = (RadioButton)findViewById(R.id.edit_jawaban_benar_2);
        jawabanEdit3 = (RadioButton)findViewById(R.id.edit_jawaban_benar_3);
        jawabanEdit4 = (RadioButton)findViewById(R.id.edit_jawaban_benar_4);

        imageLink = (ImageView)findViewById(R.id.imagRujukan);

        imageLink.setImageBitmap(null);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        mPreferenceSetting = new Shareprefered(getApplicationContext());
        cameraPhoto = new CameraPhoto(getApplicationContext());
        // Progress dialog
        pDialogs = new ProgressDialog(this);
        pDialogs.setCancelable(false);

        viewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLink();
            }
        });

        cameraLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraLink();
            }
        });

        fileLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileLink();
            }
        });

        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEdit();
            }
        });
    }

    private void viewLink(){
        String link = urlLink.getText().toString().trim();
        Intent intent = new Intent(EditQuizActivity.this, SeeRujukan.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    private void cameraLink(){
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_IMAGE_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cameraPhoto.addToGallery();
    }

    private void fileLink(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void saveEdit(){

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

        String jawaban0 = null;
        if (jawabanEdit1.isChecked()) {
            int jawabans = 1;
            jawaban0 = String.valueOf(jawabans);
        }
        if (jawabanEdit2.isChecked()) {
            int jawabans = 2;
            jawaban0 = String.valueOf(jawabans);
        }
        if (jawabanEdit3.isChecked()) {
            int jawabans = 3;
            jawaban0 = String.valueOf(jawabans);
        }
        if (jawabanEdit4.isChecked()) {
            int jawabans = 4;
            jawaban0 = String.valueOf(jawabans);
        }
        final String jaw = jawaban0;
        Log.d("qweqpoad :", jaw);

        String url = urlLink.getText().toString().trim();
        String editJawaban1 = txtPilihan1Edit1.getText().toString().trim();
        String editJawaban2 = txtPilihan1Edit2.getText().toString().trim();
        String editJawaban3 = txtPilihan1Edit3.getText().toString().trim();
        String editJawaban4 = txtPilihan1Edit4.getText().toString().trim();

        //Getting Input Data
        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");
        final String emails = email;
        String email_reference = emails;
        String id = mPreferenceSetting.getId();

        urutanQuestionlast(Image, email_reference, url, editJawaban1, editJawaban2, editJawaban3, editJawaban4, jaw, id);
    }

    public void urutanQuestionlast(final String Image, final String email_reference, final String url, final String editJawaban1, final String editJawaban2, final String editJawaban3, final String editJawaban4, final String jaw, final String id_quiz) {
        final AlertDialog tampilkandialog;
        tampilkandialog = new AlertDialog.Builder(EditQuizActivity.this).create();

        tampilkandialog.setTitle("WARNING!!!");
        tampilkandialog.setMessage("Are you sure with the edited answer ?");

        tampilkandialog.setCancelable(false);
        tampilkandialog.setCanceledOnTouchOutside(false);

        tampilkandialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Change Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        tampilkandialog.setButton(AlertDialog.BUTTON_POSITIVE, "Finish Editing",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        uploadImage(Image, email_reference, url, editJawaban1, editJawaban2, editJawaban3, editJawaban4, jaw, id_quiz);

                        Intent in = new Intent(EditQuizActivity.this, MainActivity.class);
                        startActivity(in);
                        //finish();
                    }
                });

        tampilkandialog.show();
    }

    private void uploadImage(final String Image, final String email_reference, final String url, final String editJawaban1, final String editJawaban2, final String editJawaban3, final String editJawaban4, final String jaw, final String id) {
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
                params.put(KEY_LINK, url);
                params.put(KEY_PILIHAN_1, editJawaban1);
                params.put(KEY_PILIHAN_2, editJawaban2);
                params.put(KEY_PILIHAN_3, editJawaban3);
                params.put(KEY_PILIHAN_4, editJawaban4);
                params.put(KEY_JAWABAN, jaw);
                params.put(KEY_IMAGE, Image);
                params.put(KEY_EMAIL_MAK, email_reference);
                params.put(KEY_ID, id);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


}
