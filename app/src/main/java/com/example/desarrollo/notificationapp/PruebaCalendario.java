package com.example.desarrollo.notificationapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class PruebaCalendario extends AppCompatActivity {

    private TextView tvId, tvAccountName, tvCalendarDisplayName, tvOwnerAccount;
    private Button btnEvento;
    private Integer calID;
    Cursor cur2;
    ContentResolver cr2 ;
    Uri uriEvent = CalendarContract.Events.CONTENT_URI;
    String selection2,caltimeZone,mail;
    String[] selectionArgs2;


    public static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR=1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] CAL_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.ACCOUNT_TYPE,                  // 2
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 3
            CalendarContract.Calendars.OWNER_ACCOUNT,                 // 4
            CalendarContract.Calendars.CALENDAR_TIME_ZONE             // 5
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_ACCOUNT_TYPE = 2;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 3;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 4;
    private static final int PROJECTION_TIME_ZONE_INDEX = 5;


    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,                    // 0
            CalendarContract.Events.TITLE,                  // 1
            CalendarContract.Events.DESCRIPTION,            // 2
            CalendarContract.Events.EVENT_TIMEZONE,         // 3
            CalendarContract.Events.CALENDAR_ID             // 4
    };

    // The indices for the projection array above.
    private static final int EVENT_PROJECTION_ID_INDEX = 0;
    private static final int EVENT_PROJECTION_TITLE_INDEX = 1;
    private static final int EVENT_PROJECTION_DESCRIPTION_INDEX = 2;
    private static final int EVENT_PROJECTION_EVENT_TIMEZONE_INDEX = 3;
    private static final int EVENT_PROJECTION_CALENDAR_ID_INDEX = 4;

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            //manejo de permisos
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Permiso cal otorgado", Toast.LENGTH_SHORT).show();
                    solicitocalendario(mail);
                    escribirEvento();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "Permiso write cal otorgado", Toast.LENGTH_SHORT).show();
                    escribirEvento();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_calendario);
        //recupero el mail pasado en el txtedit
        mail = getIntent().getStringExtra("mail");
        Log.d("WASAMailExtra: ", mail);
        //defino los campos
        tvId = findViewById(R.id.idTv);
        tvAccountName = findViewById(R.id.accountTv);
        tvCalendarDisplayName = findViewById(R.id.calDispNametv);
        tvOwnerAccount = findViewById(R.id.ownerTv);
        btnEvento = findViewById(R.id.btnEventos);
        // Use the cursor to step through the returned records

        Log.d("ARRANCA CALENDARIO", "OK");

        /*String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"mmvs1985@gmail.com", "com.google",
                "mmvs1985@gmail.com"};*/
        //prueba individual teniendo solo el mail.


        // chequeo los permisos si no los tengo los pido y si no ejecuto nomas
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(PruebaCalendario.this, new String[]{Manifest.permission.READ_CALENDAR}, MY_PERMISSIONS_REQUEST_READ_CALENDAR);

            Log.d("ARRANCA CALENDARIO", "ENTRO AL IF");
            return;
        }
        else{
            if(solicitocalendario(mail)>0){
                //si me devuelve un calendario escribo el evento
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PruebaCalendario.this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                    return;
                }
                else{
                   escribirEvento();
                }
            }

        }
        //cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);



        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cursor cur2;
                cr2 = getContentResolver();
                Uri uriEvent = CalendarContract.Events.CONTENT_URI;
                String selection2 = null;
                String[] selectionArgs2 = null;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(PruebaCalendario.this, new String[]{Manifest.permission.READ_CALENDAR}, MY_PERMISSIONS_REQUEST_READ_CALENDAR);

                    return;
                }
                String eventSelection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)";
                String[] eventSelectionArgs = new String[]{String.valueOf(calID),};
                cur2 = cr2.query(uriEvent, EVENT_PROJECTION, eventSelection, eventSelectionArgs, null);

                while (cur2.moveToNext()) {
                    Integer eventID;
                    String eventTitle;
                    String eventDescription;
                    String eventCalendarId;
                    String eventTimezone;

                    // Get the field values
                    eventID = cur2.getInt(EVENT_PROJECTION_ID_INDEX);
                    eventTitle= cur2.getString(EVENT_PROJECTION_TITLE_INDEX);

                    eventDescription = cur2.getString(EVENT_PROJECTION_DESCRIPTION_INDEX);
                    eventCalendarId = cur2.getString(EVENT_PROJECTION_CALENDAR_ID_INDEX);
                    eventTimezone = cur2.getString(EVENT_PROJECTION_EVENT_TIMEZONE_INDEX);
                    // Do something with the values...
                    //tvId
                    Log.d("WASAeventID", String.valueOf(eventID));
                    Log.d("WASAeventTitle", eventTitle);
                    Log.d("WASAeventDescription", eventDescription);
                    Log.d("WASAeventCalendarId", eventCalendarId);
                    Log.d("WASAeventTimezone", eventTimezone);


                }
                cur2.close();

            }
        });
    }

    private void escribirEvento() {
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();

        //    1er parcial 21.09.2018 al 29.09.2018 (*)
        //    2do parcial 17.11.2018 al 01.12.2018 (*)
        //tomar en cuenta que enero es el mes 0 entonces octubre es el mes 9
        beginTime.set(2018, 8, 21, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 8, 29, 8, 45);
        endMillis = endTime.getTimeInMillis();
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, "Semana primer parcial");
        values.put(Events.DESCRIPTION, "primeros parciales");
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, caltimeZone);
        @SuppressLint("MissingPermission")
        Uri uri = cr.insert(Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Toast.makeText(this,"Evento guardado con id:"+String.valueOf(eventID),Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private int solicitocalendarios() {
        String seleccion = null;
        String[] argumentosSelecion = null;


        // ejecuto la query
        Cursor cur;
        ContentResolver cr = getContentResolver();
        //declaro la uri a consultar
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, CAL_PROJECTION, seleccion, argumentosSelecion, null);
        int retorno=cur.getCount();
        while (cur.moveToNext()) {

            String displayName;
            String accountName;
            String ownerName;
            String accountType;
           // String timeZone;

            // Get the field values
            calID = cur.getInt(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            caltimeZone=cur.getString(PROJECTION_TIME_ZONE_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            accountType = cur.getString(PROJECTION_ACCOUNT_TYPE);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            // Do something with the values...
            //tvId
            if(caltimeZone==null){
                caltimeZone= TimeZone.getDefault().getDisplayName();
            }
            Log.d("WASACALENDARIOid", calID.toString());
            Log.d("WASACALENDARDISPLAYNAME", displayName);
            Log.d("WASACALENDARAccountName", accountName);
            Log.d("WASACALENDARACCOUNTTYPE", accountType);
            Log.d("WASACALENDARownerName", ownerName);
            Log.d("WASACALENDARtimeZone", caltimeZone);
            Log.d("WASACALENDARretorno", String.valueOf(retorno));
            tvId.setText(String.valueOf(calID));
            tvAccountName.setText(caltimeZone);
            tvCalendarDisplayName.setText(displayName);
            tvOwnerAccount.setText(ownerName);
        }
        cur.close();
        return retorno ;
    }


    @SuppressLint("MissingPermission")
    private int solicitocalendario(String mail) {
        String seleccion = null;
        String[] argumentosSelecion = null;

        if (!mail.equals("nulo")) {
            //si el mail no viene nulo lo planteo para la query sino traigo todos los calendarios
            seleccion = "(" + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?)";
            argumentosSelecion = new String[]{mail};
        }
        // ejecuto la query
        Cursor cur;
        ContentResolver cr = getContentResolver();
        //declaro la uri a consultar
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        cur = cr.query(uri, CAL_PROJECTION, seleccion, argumentosSelecion, null);
        int retorno=cur.getCount();
        if (cur!=null) {
            cur.moveToNext();

            String displayName;
            String accountName;
            String ownerName;
            String accountType;
            // String timeZone;

            // Get the field values
            calID = cur.getInt(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            caltimeZone = cur.getString(PROJECTION_TIME_ZONE_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            accountType = cur.getString(PROJECTION_ACCOUNT_TYPE);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            // Do something with the values...
            //tvId
            if (caltimeZone == null)
                caltimeZone = TimeZone.getDefault().getDisplayName();

            Log.d("WASACALENDARIOid", calID.toString());
            Log.d("WASACALENDARDISPLAYNAME", displayName);
            Log.d("WASACALENDARACCOUNTTYPE", accountType);
            Log.d("WASACALENDARownerName", ownerName);
            Log.d("WASACALENDARtimeZone", caltimeZone);
            Log.d("WASACALENDARretorno", String.valueOf(retorno));
            tvId.setText(String.valueOf(calID));
            tvAccountName.setText(caltimeZone);
            tvCalendarDisplayName.setText(displayName);
            tvOwnerAccount.setText(ownerName);

            cur.close();

        }
        return retorno;
    }

}