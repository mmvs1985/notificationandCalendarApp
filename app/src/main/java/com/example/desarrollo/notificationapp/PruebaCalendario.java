package com.example.desarrollo.notificationapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Console;
import java.util.Locale;

public class PruebaCalendario extends AppCompatActivity {
    private TextView tvId, tvAccountName, tvCalendarDisplayName, tvOwnerAccount;
    private Button btnEvento;
    private Integer calID;

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] CAL_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.ACCOUNT_TYPE,                  // 2
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 3
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 4
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_ACCOUNT_TYPE = 2;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 3;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 4;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_calendario);
        String mail = getIntent().getStringExtra("mail");
        Log.d("WASAMailExtra: ", mail);
        tvId = findViewById(R.id.idTv);
        tvAccountName = findViewById(R.id.accountTv);
        tvCalendarDisplayName = findViewById(R.id.calDispNametv);
        tvOwnerAccount = findViewById(R.id.ownerTv);
        btnEvento = findViewById(R.id.btnEventos);
        // Use the cursor to step through the returned records

        Log.d("ARRANCA CALENDARIO", "OK");
        // Run query
        Cursor cur;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        /*String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"mmvs1985@gmail.com", "com.google",
                "mmvs1985@gmail.com"};*/
        //prueba individual teniendo solo el mail.
        String selection = null;
        String[] selectionArgs = null;

        if (!mail.equals("nulo")) {
            selection = "(" + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?)";
            selectionArgs = new String[]{mail};
        }

        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("ARRANCA CALENDARIO", "ENTRO AL IF");
            return;
        }
        //cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        cur = cr.query(uri, CAL_PROJECTION, selection, selectionArgs, null);

        while (cur.moveToNext()) {

            String displayName;
            String accountName;
            String ownerName;
            String accountType;

            // Get the field values
            calID = cur.getInt(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);

            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            accountType = cur.getString(PROJECTION_ACCOUNT_TYPE);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            // Do something with the values...
            //tvId
            Log.d("WASACALENDARIOid", calID.toString());
            Log.d("WASACALENDARDISPLAYNAME", displayName);
            Log.d("WASACALENDARACCOUNTTYPE", accountType);
            Log.d("WASACALENDARownerName", ownerName);
            tvId.setText(String.valueOf(calID));
            tvAccountName.setText(accountName);
            tvCalendarDisplayName.setText(displayName);
            tvAccountName.setText(ownerName);


        }
        cur.close();
        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur2;
                ContentResolver cr2 = getContentResolver();
                Uri uriEvent = CalendarContract.Events.CONTENT_URI;
                String selection2 = null;
                String[] selectionArgs2 = null;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
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
}