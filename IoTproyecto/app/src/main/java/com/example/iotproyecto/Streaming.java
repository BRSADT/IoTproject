package com.example.iotproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Streaming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);



        WebView webview = (WebView) findViewById(R.id.webVista);




        webview.loadUrl("http://192.168.1.66:8000/index.html");
    }
}