package com.example.iotproyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AnalisisImagenes extends AppCompatActivity {
    ImageButton avatar;

    private FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 42;
    private Bitmap bitmap;
    private StorageReference mStorageRef;
    private ImageView imageView;
    private Button btnImagen;
    private StorageReference Nusuario;
    private StorageReference tipo;
    StorageReference storageRef;
    StorageReference pictureRef;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference FolderImageProc;
    private TextView letreroRangoEdad,txtRangoEdad,letreroBarba,txtBarba,letreroAnteojos,txtAnteojos,letreroOjosAbiertos,txtOjosAbiertos,letreroGenero,txtGenero,letreroBigote,txtBigote,letreroSonrisa,txtSonrisa,letreroLentesSol,txtLentesSol;
    private TextView letreroEmocion0,txtEmocion0,letreroEmocion1,txtEmocion1,letreroEmocion2,txtEmocion2,letreroEmocion3,txtEmocion3,letreroEmocion4,txtEmocion4,letreroEmocion5,txtEmocion5,letreroEmocion6,txtEmocion6,letreroEmocion7,txtEmocion7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_analisis_imagenes);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BaseDeDatos");
        FirebaseUser user = mAuth.getCurrentUser();
        user.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        tipo = storageRef.child("Procesamiento");
        FolderImageProc = storageRef.child("Procesamiento");
        avatar=(ImageButton)  findViewById(R.id.avataranalisis);
        btnImagen=(Button)  findViewById(R.id.btnimagen);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirGaleria(view);
            }
        });

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GuardarFoto();
            }
        });
        //declaraciones  Edit Text
        letreroRangoEdad=(TextView) findViewById(R.id.letreroRangoEdad);
                txtRangoEdad=(TextView) findViewById(R.id.txtRangoEdad);
        letreroBarba=(TextView) findViewById(R.id.letreroBarba);
                txtBarba=(TextView) findViewById(R.id.txtBarba);
        letreroAnteojos=(TextView) findViewById(R.id.letreroAnteojos);
                txtAnteojos=(TextView) findViewById(R.id.txtAnteojos);
        letreroOjosAbiertos=(TextView) findViewById(R.id.letreroOjosAbiertos);
                txtOjosAbiertos=(TextView) findViewById(R.id.txtOjosAbiertos);
        letreroGenero=(TextView) findViewById(R.id.letreroGenero);
                txtGenero=(TextView) findViewById(R.id.txtGenero);
        letreroBigote=(TextView) findViewById(R.id.letreroBigote);
                txtBigote=(TextView) findViewById(R.id.txtBigote);
        letreroSonrisa=(TextView) findViewById(R.id.letreroSonrisa);
                txtSonrisa=(TextView) findViewById(R.id.txtSonrisa);
        letreroLentesSol=(TextView) findViewById(R.id.letreroLentesSol);
                txtLentesSol=(TextView) findViewById(R.id.txtLentesSol);
        letreroEmocion0=(TextView) findViewById(R.id.letreroEmocion0);
                txtEmocion0=(TextView) findViewById(R.id.txtEmocion0);
        letreroEmocion1=(TextView) findViewById(R.id.letreroEmocion1);
                txtEmocion1=(TextView) findViewById(R.id.txtEmocion1);
        letreroEmocion2=(TextView) findViewById(R.id.letreroEmocion2);
                txtEmocion2=(TextView) findViewById(R.id.txtEmocion2);
        letreroEmocion3=(TextView) findViewById(R.id.letreroEmocion3);
                txtEmocion3=(TextView) findViewById(R.id.txtEmocion3);
        letreroEmocion4=(TextView) findViewById(R.id.letreroEmocion4);
                txtEmocion4=(TextView) findViewById(R.id.txtEmocion4);
        letreroEmocion5=(TextView) findViewById(R.id.letreroEmocion5);
                txtEmocion5=(TextView) findViewById(R.id.txtEmocion5);
        letreroEmocion6=(TextView) findViewById(R.id.letreroEmocion6);
                txtEmocion6=(TextView) findViewById(R.id.txtEmocion6);
        letreroEmocion7=(TextView) findViewById(R.id.letreroEmocion7);
                txtEmocion7=(TextView) findViewById(R.id.txtEmocion7);


                //Si se ha recibido algo en la bdd
        myRef = database.getReference("RespuestaAnalisis");

        myRef.child(user.getUid()).child("0").addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                obtenerRespuestaFirebase(snapshot,previousChildName);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                obtenerRespuestaFirebase(snapshot,previousChildName);

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
    public void abrirGaleria(View v){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == READ_REQUEST_CODE  && resultCode == Activity.RESULT_OK)
            try {

                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());

                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();

                avatar.setImageBitmap(bitmap.createScaledBitmap(bitmap,avatar.getWidth(),avatar.getHeight(),true)); //


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void GuardarFoto(){
        Log.i("Response", "Success");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        pictureRef = FolderImageProc.child(mAuth.getUid()+"/"+"Imagen");
        FirebaseUser user = mAuth.getCurrentUser();
        user.getUid();
        myRef.child("SolicitudImagenes").child("Usuario").setValue(new ProcImagenes(user.getUid(),"Imagen",data.toString()));

        UploadTask uploadTask = pictureRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(AnalisisImagenes.this, "No se subió", Toast.LENGTH_SHORT).show();
                Log.i("Response", "No Se subio");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(AnalisisImagenes.this, "Se subio la imagen", Toast.LENGTH_SHORT).show();

                Log.i("Response", "Se subio");
            }
        });


    }

    public void obtenerRespuestaFirebase(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){
        Log.d("snapPadre", String.valueOf(snapshot.getKey()));// dice la etiqueta, por ejemplo Age Range
        for (DataSnapshot child: snapshot.getChildren()) {//regresa ya las respuestas


          if(snapshot.getKey().equals("Emotions")) {//emociones
                   switch (child.getKey()){
                        case "0":
                            for (DataSnapshot child2: child.getChildren()) {
                                if (child2.getKey().equals("Type")){
                                    letreroEmocion0.setText(" "+child2.getValue());
                                }
                                if (child2.getKey().equals("Confidence")){
                                    txtEmocion0.setText(" "+child2.getValue()+"%");
                                }
                            }
                            break;
                        case "1":
                            for (DataSnapshot child2: child.getChildren()) {
                                if (child2.getKey().equals("Type")){
                                    letreroEmocion1.setText(" "+child2.getValue());
                                }
                                if (child2.getKey().equals("Confidence")){
                                    txtEmocion1.setText(" "+child2.getValue()+"%");
                                }
                            }
                            break;
                        case "2":
                            for (DataSnapshot child2: child.getChildren()) {
                                if (child2.getKey().equals("Type")){
                                    letreroEmocion2.setText(" "+child2.getValue());
                                }
                                if (child2.getKey().equals("Confidence")){
                                    txtEmocion2.setText(" "+child2.getValue()+"%");
                                }
                            }
                            break;
                        case "3":
                            for (DataSnapshot child2: child.getChildren()) {
                                if (child2.getKey().equals("Type")){
                                    letreroEmocion3.setText(" "+child2.getValue());
                                }
                                if (child2.getKey().equals("Confidence")){
                                    txtEmocion3.setText(" "+child2.getValue()+"%");
                                }
                            }
                            break;
                        case "4":
                            for (DataSnapshot child2: child.getChildren()) {
                                if (child2.getKey().equals("Type")){
                                    letreroEmocion4.setText(" "+child2.getValue());
                                }
                                if (child2.getKey().equals("Confidence")){
                                    txtEmocion4.setText(" "+child2.getValue()+"%");
                                }
                            }
                            break;
                        case "5":
                            for (DataSnapshot child2: child.getChildren()) {
                            if (child2.getKey().equals("Type")){
                                letreroEmocion5.setText(" "+child2.getValue());
                            }
                            if (child2.getKey().equals("Confidence")){
                                txtEmocion5.setText(" "+child2.getValue()+"%");
                            }
                        }

                            break;
                        case "6":
                            for (DataSnapshot child2: child.getChildren()) {
                                if (child2.getKey().equals("Type")){
                                    letreroEmocion6.setText(" "+child2.getValue());
                                }
                                if (child2.getKey().equals("Confidence")){
                                    txtEmocion6.setText(" "+child2.getValue()+"%");
                                }
                            }
                            break;
                        case "7":
                            for (DataSnapshot child2: child.getChildren()) {
                                if (child2.getKey().equals("Type")){
                                    letreroEmocion7.setText(" "+child2.getValue());
                                }
                                if (child2.getKey().equals("Confidence")){
                                    txtEmocion7.setText(" "+child2.getValue()+"%");
                                }
                            }
                                break;



                }
            }else{
                String padre=snapshot.getKey();
              Log.d("snapValue", String.valueOf(child.getValue()));// dice la etiqueta, por ejemplo Age Range
              Log.d("snapKey", String.valueOf(child.getKey()));// dice la etiqueta, por ejemplo Age Range

              switch (padre) {
                    case "AgeRange":
                        letreroRangoEdad.setText("Su edad parece estar entre");
                        if (child.getKey().equals("High")){
                            txtRangoEdad.setText(String.valueOf(child.getValue()));
                        }
                        if (child.getKey().equals("Low")){
                            txtRangoEdad.setText(txtRangoEdad.getText()+" y "+String.valueOf(child.getValue()));
                        }
                        break;
                    case "Beard":
                        if (child.getKey().equals("Value")){
                            if (String.valueOf(child.getValue()).equals("true")){
                            letreroBarba.setText("Parece ser barbon");
                            }
                            if (String.valueOf(child.getValue()).equals("false")){
                                letreroBarba.setText("Parece no tener Barba");
                            }
                        }
                        if (child.getKey().equals("Confidence")){
                            txtBarba.setText(" Fiabilidad del "+child.getValue());
                        }
                        break;
                    case "Eyeglasses":
                        Log.d("snapRes", "AAAnteojos");// dice la etiqueta, por ejemplo Age Range

                        if (child.getKey().equals("Value")){
                            Log.d("snapResValueProb", String.valueOf(child.getValue()));// dice la etiqueta, por ejemplo Age Range

                            if (String.valueOf(child.getValue()).equals("true")){   Log.d("snapRes", "No anteojos");// dice la etiqueta, por ejemplo Age Range

                                letreroAnteojos.setText("Parece usar anteojos");

                            }
                            if (String.valueOf(child.getValue()).equals("false")){
                                Log.d("snapRes", "Sí anteojos");// dice la etiqueta, por ejemplo Age Range

                                letreroAnteojos.setText("Parece no usar anteojos");


                            }
                        }
                        if (child.getKey().equals("Confidence")){
                            txtAnteojos.setText(" Fiabilidad del "+child.getValue());
                        }
                        break;
                    case "EyesOpen":
                        if (child.getKey().equals("Value")){
                            if (child.getValue().equals("True")){
                                letreroOjosAbiertos.setText("Parece tener los ojos abiertos");
                            }
                            if (child.getValue().equals("False")){
                                letreroOjosAbiertos.setText("Parece no tener los ojos abiertos");
                            }
                        }
                        if (child.getKey().equals("Confidence")){
                            txtOjosAbiertos.setText(" Fiabilidad del "+child.getValue());
                        }
                        break;
                    case "Gender":
                        if (child.getKey().equals("Value")){
                            if (child.getValue().equals("Male")){
                                letreroGenero.setText("Parece ser Hombre");
                            }
                            if (child.getValue().equals("Female")){
                                letreroGenero.setText("Parece ser Mujer");
                            }
                        }
                        if (child.getKey().equals("Confidence")){
                            txtGenero.setText(" Fiabilidad del "+child.getValue());
                        }
                        break;
                    case "MouthOpen":

                        //falta
                        break;
                    case "Mustache":
                        if (child.getKey().equals("Value")){
                            if (child.getValue().equals("tTrue")){
                                letreroBigote.setText("Parece tener bigote");
                            }
                            if (child.getValue().equals("False")){
                                letreroBigote.setText("Parece no tener bigote");
                            }
                        }
                        if (child.getKey().equals("Confidence")){
                            txtBigote.setText(" Fiabilidad del "+child.getValue());
                        }
                            break;
                    case "Smile":
                        if (child.getKey().equals("Value")){
                            if (child.getValue().equals("true")){
                                letreroSonrisa.setText("Parece estar sonriendo");
                            }
                            if (child.getValue().equals("false")){
                                letreroSonrisa.setText("Parece no estar sonriendo");
                            }
                        }
                        if (child.getKey().equals("Confidence")){
                            txtSonrisa.setText(" Fiabilidad del "+child.getValue());
                        }
                        break;
                    case "Sunglasses":
                        if (child.getKey().equals("Value")){
                            if (child.getValue().equals("true")){
                                letreroLentesSol.setText("Parece no tener lentes de sol");
                            }
                            if (child.getValue().equals("false")){
                                letreroLentesSol.setText("Parece no tener lentes de sol");
                            }
                        }
                        if (child.getKey().equals("Confidence")){
                            txtBigote.setText(" Fiabilidad del "+child.getValue());
                        }
                        break;
                }


            }
        }
    }
}