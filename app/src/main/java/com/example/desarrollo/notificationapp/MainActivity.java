package com.example.desarrollo.notificationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView)findViewById(R.id.infoTV);
        if(getIntent().getExtras()!=null){
            //for(String key : getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString("clave");
                tv.append("\n"+"clave: "+" : "+value);
          //  }
        }
    }
}
