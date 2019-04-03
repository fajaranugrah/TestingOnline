package com.example.fajar.testingonline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class SeeImage extends AppCompatActivity {

    WebView imageSumber;
    String imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_image);

        imageSumber = (WebView) findViewById(R.id.imageSumber);

        Bundle q = new Bundle();
        q = getIntent().getExtras();
        imageLink = q.getString("imageLink");

        imageSumber.loadUrl(imageLink);
    }
}
