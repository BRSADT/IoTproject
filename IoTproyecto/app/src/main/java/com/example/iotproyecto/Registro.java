package com.example.iotproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Button btnRegistrarse;
    Button btnInicio;
    private FirebaseAuth mAuth;
    public String cuenta, pass;
    public EditText txtCuenta, txtContra, txtNombre;
    FirebaseDatabase database;
    DatabaseReference myRef;
    // private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_registro);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BaseDeDatos");
        super.onCreate(savedInstanceState);

        txtCuenta = (EditText) findViewById(R.id.txtCuenta);
        txtContra = (EditText) findViewById(R.id.txtContra);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        mAuth = FirebaseAuth.getInstance();
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuenta = txtCuenta.getText().toString();
                pass = txtContra.getText().toString();
                Registrar();
            }
        });

        btnInicio = (Button) findViewById(R.id.btnInicio);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registro.this, MainActivity.class);
                startActivity(i);

            }
        });

    }



    public void Registrar() {
        mAuth.createUserWithEmailAndPassword(cuenta, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User registered successfully
                            Log.i("Response", "Success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.getUid();
                            String nombre=txtNombre.getText().toString();
                            myRef.child("Usuarios").child(user.getUid()).setValue(new Usuario(nombre,cuenta))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Write was successful!
                                            // ...
                                            Toast.makeText(Registro.this, "Registrado en BDD", Toast.LENGTH_SHORT).show();
                                            Log.i("Response",  "Registrado en BDD");

                                            Intent i = new Intent(Registro.this, Menu.class);
                                            startActivity(i);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Registro.this, "Error en bdd", Toast.LENGTH_SHORT).show();
                                            Log.i("Response",  "Error en bdd");
                                        }
                                    });

                            Toast.makeText(Registro.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Registro.this, "Failed to create user:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("Response",  task.getException().getMessage());

                        }

                        // ...

                    }


                });
    }
}