package com.example.john117.bottomtabtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class FormActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences userPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ImageView profile = (ImageView)findViewById(R.id.imageView3);
        EditText firstName = (EditText)findViewById(R.id.editText);
        EditText lastName = (EditText)findViewById(R.id.editText1);
        EditText email = (EditText)findViewById(R.id.editText2);
        final EditText Weight = (EditText)findViewById(R.id.editText4);
        final EditText Height = (EditText)findViewById(R.id.editText3);
        Button submit = (Button)findViewById(R.id.button2);
        sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        userPreferences = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
        firstName.setText(sharedPreferences.getString("UserName",null));
        email.setText(sharedPreferences.getString("Email",null));
        Glide.with(getApplicationContext()).load(sharedPreferences.getString("PersonPhoto",null))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profile);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = userPreferences.edit();
                Toast.makeText(FormActivity.this,"Weight " + Weight.getText().toString() + "Height " + Height.getText().toString(),Toast.LENGTH_LONG).show();
                editor.putString("Weight",Weight.getText().toString());
                editor.putString("Height",Height.getText().toString());

                int weight = Integer.parseInt(Weight.getText().toString());
                int height = Integer.parseInt(Height.getText().toString());
                float BMI = (float) (weight * 10000) /(float) (height * height);

                editor.putString("BMI", "" + BMI);
                Toast.makeText(FormActivity.this,"" + BMI,Toast.LENGTH_LONG).show();
                editor.commit();
                Intent i = new Intent(FormActivity.this,MainActivity.class);
                startActivity(i);
            }

            });


    }

}
