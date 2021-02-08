package com.example.iotproyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Historial extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    LinearLayout.LayoutParams LParamsContenido ;
    LinearLayout LayerHistorial;
    ArrayList<LinearLayout> layoutContenido=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutFotografia=new ArrayList<LinearLayout>();
    ArrayList<ImageView> Fotografias=new ArrayList<ImageView>();
    ArrayList<LinearLayout> layoutSeparadorFotografia=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutDatos=new ArrayList<LinearLayout>();
    ArrayList<TextView> TipoAlerta=new ArrayList<TextView>();
    ArrayList<TextView> Flama=new ArrayList<TextView>();
    ArrayList<TextView> diaHora=new ArrayList<TextView>();
    ArrayList<TextView> distancia=new ArrayList<TextView>();
    ArrayList<TextView> NombreIdentificado=new ArrayList<TextView>();
    View HistorialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        LayerHistorial=(LinearLayout)  findViewById(R.id.LayerHistorial);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BaseDeDatos");
        HistorialView = LayoutInflater.from(this).inflate(R.layout.activity_historial,null, false);
        myRef.child("HistorialSensores").addChildEventListener(new ChildEventListener() {
        Contenido =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1.0f);
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Datos(snapshot,previousChildName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Datos(snapshot,previousChildName);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void Datos(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
        Log.d("snapPadre", String.valueOf(snapshot.getKey()));// dice la etiqueta, por ejemplo Age Range
        layoutContenido.add(new LinearLayout(HistorialView.getContext()));
        layoutContenido.get(layoutContenido.size()-1).setLayoutParams();
        for (DataSnapshot child: snapshot.getChildren()) {//regresa ya las respuestas
            Log.d("snap Key", String.valueOf(child.getKey()));// dice la etiqueta, por ejemplo Age Range
            Log.d("snap Value", String.valueOf(child.getValue()));// dice la etiqueta, por ejemplo Age Range

        }

    }
}