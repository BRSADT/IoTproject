package com.example.iotproyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import static com.example.iotproyecto.APP.CHANNEL_1_ID;
public class Menu extends AppCompatActivity {
    LinearLayout btnStreaming;
    LinearLayout procImagenes;
    LinearLayout Historial;
    LinearLayout configuracionSensores;
    private FirebaseAuth mAuth;

    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnStreaming = (LinearLayout) findViewById(R.id.irStreaming);
        procImagenes= (LinearLayout) findViewById(R.id.procImagenes);
        configuracionSensores= (LinearLayout) findViewById(R.id.configuracionSensores);
        Historial= (LinearLayout) findViewById(R.id.Historial);
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
        configuracionSensores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, ConfiguracionSensores.class);
                startActivity(i);
            }
        });
        Historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, Historial.class);
                startActivity(i);
            }
        });
        notificationManager = NotificationManagerCompat.from(this);  //resolver que solo se ejecute una vez
        startService(new Intent(Menu.this, Servicio.class));

    }
}