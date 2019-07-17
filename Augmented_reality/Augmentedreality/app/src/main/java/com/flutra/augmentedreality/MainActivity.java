package com.flutra.augmentedreality;


import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.flutra.augmentedreality.constants.AppConstatnts;
import com.flutra.augmentedreality.fragment.CurrentWeatherFragment;
import com.flutra.augmentedreality.fragment.LastCityFragment;
import com.flutra.augmentedreality.fragment.WeeklyWeatherFragment;

public class MainActivity extends AppCompatActivity {

//    private final static String BASE_URL = AppConstatnts.FIRST_BASE_URL;
//    private final static String API_KEY = "4985cef944555b5dd6bd6496f8f519c7";



    ImageView currentButton;
    ImageView weeklyButton;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        currentButton = findViewById(R.id.today_btn);
        weeklyButton = findViewById(R.id.all_week_btn);
        sharedPref = getSharedPreferences(AppConstatnts.SHARED_PREF, MODE_PRIVATE);

        String currentCity = sharedPref.getString(AppConstatnts.CITY_CURRENT, "doesnotexist");

        if (currentCity == null || currentCity.equals("doesnotexist"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentWeatherFragment()).commit();

        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LastCityFragment()).commit();
        }



        currentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                currentButton.setBackground(highlight);
                weeklyButton.setBackground(null);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentWeatherFragment()).commit();

            }
        });

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                 weeklyButton.setBackground(highlight);
                 currentButton.setBackground(null);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WeeklyWeatherFragment()).commit();
            }
        });






    }
}
