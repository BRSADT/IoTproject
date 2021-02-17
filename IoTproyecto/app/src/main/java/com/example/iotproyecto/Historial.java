package com.example.iotproyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Historial extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    StorageReference pictureRef;
    FirebaseDatabase database;
    DatabaseReference myRef;
    LinearLayout.LayoutParams LParamsContenido,LParamsImagen,LParamsSeparador,LParamsInformacion,LParamsTexto;
    LinearLayout LayerHistorial;
    ArrayList<LinearLayout> layoutContenido=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> SeparadorContenido1=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> SeparadorContenido2=new ArrayList<LinearLayout>();
    ArrayList<StorageReference> referencias=new ArrayList<StorageReference>();
    ArrayList<LinearLayout> layoutFotografia=new ArrayList<LinearLayout>();
    ArrayList<ImageView> Fotografias=new ArrayList<ImageView>();
    ArrayList<LinearLayout> layoutSeparadorFotografia=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutDatos=new ArrayList<LinearLayout>();
    ArrayList<TextView> TipoAlerta=new ArrayList<TextView>();
    ArrayList<TextView> Flama=new ArrayList<TextView>();
    ArrayList<TextView> diaHora=new ArrayList<TextView>();
    ArrayList<TextView> distancia=new ArrayList<TextView>();
    ArrayList<TextView> NombreIdentificado=new ArrayList<TextView>();
    ArrayList<Task> refeStorage=new ArrayList<Task>();
    ScrollView sv;
    ImageView imageView6;
    Button btnRegistro;
    View HistorialView;
    int Control;
    StorageReference FolderImageProc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
         database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BaseDeDatos");
        sv = (ScrollView)findViewById(R.id.scroll);
        Control=0;
        btnRegistro=(Button)  findViewById(R.id.btnRegistroDatos);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRegistro.setEnabled(false);
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("BaseDeDatos");
                myRef.child("TomarFoto").setValue("Si");

            }
        });


       // HistorialView = LayoutInflater.from(this).inflate(R.layout.activity_historial,null, false);
        LParamsContenido =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150, 0);
        LParamsImagen =   new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1.5f);
        LParamsSeparador =   new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, .5f);
        LParamsInformacion =   new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LParamsTexto =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1.0f);

        LayerHistorial=(LinearLayout)  findViewById(R.id.LayerHistorial);



        Log.i("bandera","antes");
        myRef.child("HistorialSensores").addChildEventListener(new ChildEventListener() {

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

        Log.i("bandera","despues");
        myRef.child("TomarFoto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("HistorialFotoEntro", String.valueOf(dataSnapshot.getKey()+Control));// dice la etiqueta, por ejemplo Age Range
                 if (Control!=0){
                Log.d("HistorialFotoNo", String.valueOf(dataSnapshot.getValue()));// dice la etiqueta, por ejemplo Age Range
                /*if (String.valueOf(dataSnapshot.getValue()).equals("No")){
                    finish();
                    startActivity(getIntent());

                }*/

                btnRegistro.setEnabled(true);
                // }

                    // sv.scrollTo(0, sv.getBottom());
                     sv.scrollTo(0, sv.getBottom());
            }else {
                     Control = 1;
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
            });
    }

    public void Datos(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
        Log.d("snapPadre", String.valueOf(snapshot.getKey()));// dice la etiqueta, por ejemplo Age Range
        layoutContenido.add(new LinearLayout(Historial.this));
        //layoutContenido.get(layoutContenido.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        layoutContenido.get(layoutContenido.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,250, 0));

        layoutContenido.get(layoutContenido.size()-1).setOrientation(LinearLayout.HORIZONTAL);

        SeparadorContenido1.add(new LinearLayout(Historial.this));
        SeparadorContenido1.get(SeparadorContenido1.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,30, 0));
        SeparadorContenido1.get(SeparadorContenido1.size()-1).setOrientation(LinearLayout.HORIZONTAL);


        layoutSeparadorFotografia.add(new LinearLayout(Historial.this));
        layoutSeparadorFotografia.get(layoutSeparadorFotografia.size()-1).setLayoutParams( new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 0.2f));
        layoutSeparadorFotografia.get(layoutSeparadorFotografia.size()-1).setOrientation(LinearLayout.HORIZONTAL);


        layoutDatos.add(new LinearLayout(Historial.this));
        layoutDatos.get(layoutDatos.size()-1).setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        layoutDatos.get(layoutDatos.size()-1).setOrientation(LinearLayout.VERTICAL);


        for (DataSnapshot child: snapshot.getChildren()) {//regresa ya las respuestas
            Log.d("snap Key", String.valueOf(child.getKey()));// dice la etiqueta, por ejemplo Age Range
            Log.d("snap Value", String.valueOf(child.getValue()));// dice la etiqueta, por ejemplo Age Range
            switch (child.getKey()) {
                case "Fecha":
                    diaHora.add(new TextView(Historial.this));
                    diaHora.get(diaHora.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1.0f));
                    diaHora.get(diaHora.size()-1).setText(child.getValue().toString());
                    break;
                case "Hora":
                   diaHora.get(diaHora.size()-1).setText(diaHora.get(diaHora.size()-1).getText()+" "+child.getValue().toString());
                   layoutDatos.get(layoutDatos.size()-1).addView(diaHora.get(diaHora.size()-1));
                   break;
               case "Ruta":

                    Fotografias.add(new ImageView(Historial.this));
                    Fotografias.get(Fotografias.size()-1).setLayoutParams(new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT, 1.5f));
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    pictureRef=storage.getReference().child((child.getValue().toString()));
                  GlideApp.with(Historial.this)
                           .load(pictureRef)
                           .into(Fotografias.get(Fotografias.size()-1));

                            layoutContenido.get(layoutContenido.size() - 1).addView(Fotografias.get(Fotografias.size()-1));

                   break;
                case "ResponseDeteccion":
                    String res="";
                    Log.d("snapValorDet", child.getKey() );// dice la etiqueta, por ejemplo Age Range
                    if (child.hasChildren()){
                            for (DataSnapshot child2: child.getChildren()){

                                Log.d("snapc2Valor", child2.getKey() ); //regresa 0
                                Log.d("snapc2Valor", child2.getValue().toString());
                                 res="";
                                switch(child2.getKey()){
                                    case "0":

                                        for (DataSnapshot child3: child2.getChildren()){
                                            switch (child3.getKey()) {
                                                case "Similarity":
                                                    res+=child3.getValue().toString();
                                                    break;
                                                case "Face":
                                                    for (DataSnapshot child4: child3.getChildren()){
                                                        Log.d("snapc3Valor", child4.getValue().toString());

                                                        switch (child4.getKey()) {
                                                            case "ExternalImageId":
                                                                res=child4.getValue().toString()+" "+res;
                                                                break;
                                                        }
                                                    }

                                                    break;
                                            }
                                        }
                                    break;

                                }
                                Log.d("snapRes", res);
                                NombreIdentificado.add(new TextView(Historial.this));
                                NombreIdentificado.get(NombreIdentificado.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1.0f));
                                NombreIdentificado.get(NombreIdentificado.size()-1).setText(res);
                                layoutDatos.get(layoutDatos.size()-1).addView(NombreIdentificado.get(NombreIdentificado.size()-1));

                            }

                    }else {
                         res = child.getValue().toString();
                        Log.d("snap Res", res);
                        NombreIdentificado.add(new TextView(Historial.this));
                        NombreIdentificado.get(NombreIdentificado.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1.0f));
                        NombreIdentificado.get(NombreIdentificado.size()-1).setText(res);
                        layoutDatos.get(layoutDatos.size()-1).addView(NombreIdentificado.get(NombreIdentificado.size()-1));

                    }


                    break;

                case "Tipo":
                    TipoAlerta.add(new TextView(Historial.this));
                    TipoAlerta.get(TipoAlerta.size()-1).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1.0f));
                    TipoAlerta.get(TipoAlerta.size()-1).setText(child.getValue().toString());
                    layoutDatos.get(layoutDatos.size()-1).addView(TipoAlerta.get(TipoAlerta.size()-1));

                   break;

            }

        }

        layoutContenido.get(layoutContenido.size()-1).addView(layoutSeparadorFotografia.get(layoutSeparadorFotografia.size()-1));
        layoutContenido.get(layoutContenido.size()-1).addView(layoutDatos.get(layoutDatos.size()-1));
        LayerHistorial.addView(layoutContenido.get(layoutContenido.size()-1));
        LayerHistorial.addView(SeparadorContenido1.get(SeparadorContenido1.size()-1));
      //  sv.scrollTo(0, sv.getBottom());
    }

    public void Tfoto(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
        Log.d("HistorialFotoEntro", String.valueOf(snapshot.getKey()));// dice la etiqueta, por ejemplo Age Range

      //  if (Control!=0){
        Log.d("HistorialFotoNo", String.valueOf(snapshot.getValue()));// dice la etiqueta, por ejemplo Age Range
        if (String.valueOf(snapshot.getValue()).equals("No")){
            finish();
            startActivity(getIntent());

        }
        sv.scrollTo(0, sv.getBottom());
           btnRegistro.setEnabled(true);
      // }
        Control=1;
    }
}