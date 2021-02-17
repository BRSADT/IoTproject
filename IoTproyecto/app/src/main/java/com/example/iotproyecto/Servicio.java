package com.example.iotproyecto;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.iotproyecto.APP.CHANNEL_1_ID;
import java.io.IOException;
import java.net.ServerSocket;



public class Servicio  extends Service {
    DatabaseReference myRef;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private NotificationManagerCompat notificationManager;
    int control=0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BaseDeDatos");
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = NotificationManagerCompat.from(Servicio.this);
        Log.i("Servicio", "Start");
       //          notificationManager = NotificationManagerCompat.from(Servicio.this);
         //               sendOnChannel1();
        myRef = database.getReference("BaseDeDatos");
        myRef.child("Sensores").addChildEventListener(new ChildEventListener() {
         @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("Servicio", "Cambio");
                Alertas(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("Servicio", "Cambio");
                Alertas(snapshot);

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Alertas(snapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return START_STICKY;
    }
    public void Alertas(@NonNull DataSnapshot snapshot){
       if (control!=0) {

           switch (snapshot.getValue().toString()){
               case "Llama":
                   sendOnChannel1();
                   break;
               case "Movimiento":
                   sendOnChannel3();
                   break;
               case "Lluvia":
                   sendOnChannel2();
                   break;
           }
       }else{
           control=1;

       }
    }
    public void sendOnChannel1() { //NOTIFICATION 1
        String title = "Fuego";
        String message = "Se ha detectado fuego";

        Notification notification = new NotificationCompat.Builder(Servicio.this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.fuego)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(30, notification);
    }
    public void sendOnChannel2() {  //NOTIFICATION 2
        Log.i("Mens","aqui2");
        String title = "Lluvia";
        String message = "Corre y mete la ropa";
        Notification notification = new NotificationCompat.Builder(Servicio.this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.lluvia1)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(35, notification);
    }
    public void sendOnChannel3() { //NOTIFICATION 3
        String title = "Movimiento";
        String message = "Se ha detectado movimiento";

        Notification notification = new NotificationCompat.Builder(Servicio.this, CHANNEL_1_ID)
                 .setSmallIcon(R.drawable.movimiento)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(40, notification);
    }
}
