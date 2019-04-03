package com.example.fajar.testingonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class SeeRujukan extends AppCompatActivity {

    private WebView webView;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_rujukan);

        webView = (WebView)findViewById(R.id.webView);

        Bundle q = new Bundle();
        q = getIntent().getExtras();
        link = q.getString("link");

        webView.loadUrl(link);
    }
}
