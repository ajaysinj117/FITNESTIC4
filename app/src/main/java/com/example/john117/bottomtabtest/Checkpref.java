package com.example.john117.bottomtabtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Checkpref extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpref);
        String name;
        setContentView(R.layout.activity_checkpref);
        TextView txt = (TextView)findViewById(R.id.textView3);
        sharedpreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("UserName"))
        { name = sharedpreferences.getString("UserName",null);
            txt.setText(name);}
        else
            txt.setText("shi");
    }
}
