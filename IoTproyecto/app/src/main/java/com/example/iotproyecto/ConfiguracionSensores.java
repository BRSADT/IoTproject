package com.example.iotproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ConfiguracionSensores extends AppCompatActivity {


    Switch modoAlerta,AlertaLLuvia;
    EditText inputSegundos, inputNombre;
    Button btnEnviar;
    ImageButton imageButton;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 42;
    private Bitmap bitmap = null;
    private FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    StorageReference pictureRef;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference FolderImageProc;
    String Slluvia,Sllama,SmodoAlerta;
    FirebaseUser user;
    byte[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_sensores);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        inputSegundos = (EditText) findViewById(R.id.inputSegundos);
        inputNombre = (EditText) findViewById(R.id.inputNombre);
        modoAlerta = (Switch) findViewById(R.id.modoAlerta);
//        Alertallama = (Switch) findViewById(R.id.Alertallama);
        AlertaLLuvia = (Switch) findViewById(R.id.AlertaLLuvia);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirGaleria(view);

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (inputSegundos.getText().toString().equals("")) {
                    Toast.makeText(ConfiguracionSensores.this, "Te hace falta agregar datos", Toast.LENGTH_SHORT).show();
                } else {
                    if (!inputNombre.getText().toString().equals("")) {

                        if (bitmap != null) {
                            Toast.makeText(ConfiguracionSensores.this, "Se subira foto", Toast.LENGTH_SHORT).show();
                            subirDatos(true);

                        } else {
                            Toast.makeText(ConfiguracionSensores.this, "Ponga una foto", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ConfiguracionSensores.this, "No agregara a colección", Toast.LENGTH_SHORT).show();
                        subirDatos(false);

                    }
                }
            }
        });

        //firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BaseDeDatos");

    }


    public void abrirGaleria(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {

                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());

                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();

                imageButton.setImageBitmap(bitmap.createScaledBitmap(bitmap, imageButton.getWidth(), imageButton.getHeight(), true)); //


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void subirDatos(boolean Foto) {


        Slluvia = AlertaLLuvia.isChecked() == true ? "true" : "false";
        SmodoAlerta = modoAlerta.isChecked() == true ? "true" : "false";
        if (Foto) {
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
            FolderImageProc = storageRef.child("PersonasReconocidas");


            Log.i("Response", "Success");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
            pictureRef = FolderImageProc.child(mAuth.getUid() + "/" + inputNombre.getText().toString()); //subir foto


            user = mAuth.getCurrentUser();
            user.getUid();
            Toast.makeText(ConfiguracionSensores.this, "Se subira", Toast.LENGTH_SHORT).show();



            UploadTask uploadTask = pictureRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(ConfiguracionSensores.this, "No se subió", Toast.LENGTH_SHORT).show();
                    Log.i("Response", "No Se subio");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(ConfiguracionSensores.this, "Se subio la imagen", Toast.LENGTH_SHORT).show();
                    myRef.child("CambioSensoresFoto").child("Usuario").setValue(new ConfigSensores(inputNombre.getText().toString(),SmodoAlerta,inputSegundos.getText().toString(),user.getUid(),data.toString(),Sllama,Slluvia));

                    Log.i("Response", "Se subio");
                }
            });



        } else {


                myRef.child("CambioSensores").child("Usuario").setValue(new ConfigSensores(SmodoAlerta,inputSegundos.getText().toString(),Sllama,Slluvia));

            Log.i("Response", "Se subio");

        }


    }

}



