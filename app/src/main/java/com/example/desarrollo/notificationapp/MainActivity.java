package com.example.desarrollo.notificationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private EditText eTmail;
    private Button btnTodoCal, btnMailPartiuclar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= findViewById(R.id.infoTV);
        btnTodoCal =findViewById(R.id.button);
        btnMailPartiuclar=findViewById(R.id.esteCalBtn);
        eTmail=findViewById(R.id.mailET);
        if(getIntent().getExtras()!=null){
            //for(String key : getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString("clave");
                tv.append("\n"+"clave: "+" : "+value);
          //  }
        }
        btnMailPartiuclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),PruebaCalendario.class);
                in.putExtra("mail",eTmail.getText().toString());
                startActivity(in);
            }
        });
        btnTodoCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),PruebaCalendario.class);
                in.putExtra("mail","nulo");
                startActivity(in);
            }
        });
    }
}
