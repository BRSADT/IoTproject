package com.example.iotproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {
    LinearLayout btnStreaming;
    LinearLayout procImagenes;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnStreaming = (LinearLayout) findViewById(R.id.irStreaming);
        procImagenes= (LinearLayout) findViewById(R.id.procImagenes);
        mAuth = FirebaseAuth.getInstance();
        Log.d("datos", mAuth.getUid());

        btnStreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, Streaming.class);
                startActivity(i);
            }
        });
        procImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, AnalisisImagenes.class);
                startActivity(i);
            }
        });
    }
}