package com.example.desarrollo.notificationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView)findViewById(R.id.infoTV);
        btn=findViewById(R.id.button);
        if(getIntent().getExtras()!=null){
            //for(String key : getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString("clave");
                tv.append("\n"+"clave: "+" : "+value);
          //  }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),PruebaCalendario.class);
                startActivity(in);
            }
        });
    }
}
