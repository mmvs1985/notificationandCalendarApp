package com.example.desarrollo.notificationapp;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class miServiciodeMensajeriaFirebase extends FirebaseMessagingService {
    //implementa el metodo que se ejecuta cuando recibimos el mensaje

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        String De = remoteMessage.getFrom();
        if(remoteMessage.getNotification()!=null){
            String titulo = remoteMessage.getNotification().getTitle();
            String cuerpo = remoteMessage.getNotification().getBody();
                Log.d("prueba","Notificacion: "+cuerpo);
            mostrarNotificacion(titulo,cuerpo);
        }
        else Log.d("prueba","Notificacion fallida");

        if(remoteMessage.getData().size()>0){
            Map<String,String> Datos =remoteMessage.getData();
            Log.d("prueba","Datos: "+Datos);
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("prueba","Token: "+s);
    }

    public void mostrarNotificacion(String titulo, String cuerpo){

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);



    }
}
