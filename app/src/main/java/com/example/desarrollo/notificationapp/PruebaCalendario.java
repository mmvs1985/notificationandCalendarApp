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
import android.widget.TextView;

import java.io.Console;

public class PruebaCalendario extends AppCompatActivity {
    private TextView tvId;
    private TextView tvAccountName;
    private TextView tvCalendarDisplayName;
    private TextView tvOwnerAccount;

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_calendario);
        tvId = (TextView)findViewById(R.id.idTv);
        tvAccountName = (TextView)findViewById(R.id.accountTv);
        tvCalendarDisplayName = (TextView)findViewById(R.id.calDispNametv);
        tvOwnerAccount = (TextView)findViewById(R.id.ownerTv);
        // Use the cursor to step through the returned records

        Log.d("ARRANCA CALENDARIO","OK");
        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"mmvs1985@gmail.com", "com.google",
                "mmvs1985@gmail.com"};
        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("ARRANCA CALENDARIO","ENTRO AL IF");
            return;
        }
        //cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        while (cur.moveToNext()) {
            Integer calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
            String accountType=null;

            // Get the field values
            calID = cur.getInt(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);

            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            accountType =cur.getString(PROJECTION_ACCOUNT_TYPE);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            // Do something with the values...
            //tvId
            Log.d("WASACALENDARIOid",calID.toString());
            Log.d("WASADISPLAYNAME",displayName);
            Log.d("WASAACCOUNTTYPE",accountType);
            Log.d("WASAownerName",ownerName);
            tvId.setText(calID.toString());
            tvAccountName.setText(accountName);
            tvCalendarDisplayName.setText(displayName);
            tvAccountName.setText(ownerName);


        }
    }
}