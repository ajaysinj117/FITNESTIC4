package com.example.john117.bottomtabtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class HomeFragment extends Fragment {
    TextView userinfo1;
    TextView userinfo2;
    TextView userinfo3;
    TextView userinfo4;
    TextView userinfo5;
    ImageView Profile;
    TextView email;
    TextView name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences;
        SharedPreferences userPreferences;
        View v = inflater.inflate(R.layout.frag_home, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("UserName")) {
            userinfo1 = (TextView)v.findViewById(R.id.user_info1);
            userinfo2 = (TextView)v.findViewById(R.id.user_info2);
            userinfo3 = (TextView)v.findViewById(R.id.user_info3);
            userinfo4 = (TextView)v.findViewById(R.id.user_info4);
            userinfo5 = (TextView)v.findViewById(R.id.user_info5);
            email = (TextView)v.findViewById(R.id.user_profile_short_bio);
            Profile = (ImageView) v.findViewById(R.id.user_profile_photo);
            name = (TextView)v.findViewById(R.id.user_profile_name);
            name.setText(sharedPreferences.getString("UserName", null));
            email.setText(sharedPreferences.getString("Email", null));
            Glide.with(getActivity().getApplicationContext()).load(sharedPreferences.getString("PersonPhoto", null))
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Profile);
            String Weight = sharedPreferences.getString("Weight", null);
            String Height = sharedPreferences.getString("Height", null);
            String BMI = sharedPreferences.getString("BMI",null);
            //float BMI = weight / (height * height);
            //editor.putString("BMI", "" + BMI).commit();
            userinfo1.setText("Weight " + Weight);
            userinfo2.setText("Height " + Height);
            userinfo3.setText("BMI " + BMI);
            //userinfo3.setText("BMI " + BMI);
        }
        return v;
    }
}