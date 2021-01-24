package com.example.iotproyecto;

import androidx.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    StorageReference FolderImageProc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_analisis_imagenes);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();

       //  Nusuario = storageRef.child(mAuth.getUid());//nulo
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        tipo = storageRef.child("Procesamiento");

        FolderImageProc = storageRef.child("Procesamiento");

// Child references can also take paths
// spaceRef now points to "images/space.jpg
// imagesRef still points to "images"




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
        pictureRef = FolderImageProc.child(mAuth.getUid()+"/"+"imagen");

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
}