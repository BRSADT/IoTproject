package com.example.iotproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Streaming extends AppCompatActivity implements LifecycleObserver {
    FirebaseDatabase database;
    DatabaseReference myRef;
    FloatingActionButton refresh;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        String camara="Streaming";
        FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.refresh);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BaseDeDatos");
        myRef.child("Camara").setValue(camara);
         webview = (WebView) findViewById(R.id.webVista);
        Toast.makeText(this,"In Foreground",Toast.LENGTH_LONG).show(); //activar camara

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl("http://192.168.1.200:8000/index.html");

            }
        });
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);


        //webview.loadUrl("http://192.168.1.200:8000/index.html");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void appInResumeState() {
        //Toast.makeText(this,"In Foreground",Toast.LENGTH_LONG).show(); //activar camara
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void appInPauseState() {
        Toast.makeText(this,"In Background",Toast.LENGTH_LONG).show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)//AL SALIR
    public void appInStopState() {
        Toast.makeText(this,"In stop",Toast.LENGTH_LONG).show(); //desactivar camara
        String camara="NoStreaming";
        myRef.child("Camara").setValue(camara);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void appInDestroyState() {
        Toast.makeText(this,"In destroy",Toast.LENGTH_LONG).show();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void appInCreateState() {
        Toast.makeText(this,"In create",Toast.LENGTH_LONG).show();//activar camara
        String camara="Streaming";
        myRef.child("Camara").setValue(camara);
    }

    public void onBackPressed(){
        Toast.makeText(this,"Atras",Toast.LENGTH_LONG).show(); //hacer que se vaya a menu y desactivar camara
        Intent i = new Intent(Streaming.this, Menu.class);
        String camara="NoStreaming";
        myRef.child("Camara").setValue(camara);
        startActivity(i);
    }
}