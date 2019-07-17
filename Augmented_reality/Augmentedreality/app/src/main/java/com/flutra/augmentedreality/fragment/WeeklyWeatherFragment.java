package com.flutra.augmentedreality.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import com.flutra.augmentedreality.ApiInterface;
import com.flutra.augmentedreality.CityMap;
import com.flutra.augmentedreality.Coord;
import com.flutra.augmentedreality.R;
import com.flutra.augmentedreality.adapters.RecViewAdapter;
import com.flutra.augmentedreality.constants.AppConstatnts;
import com.flutra.augmentedreality.models.CurrentResponse;
import com.flutra.augmentedreality.models.Datum_;
import com.flutra.augmentedreality.utils.DateFormater;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeeklyWeatherFragment extends Fragment {

    private static View rootView;
    private SharedPreferences sharedPref;
    private RecyclerView recyclerView;
    private RecViewAdapter adapter;
    private static ArrayList<Datum_> datum_s;
    static TextView temperatureTxt;
    static ImageView currentImage;
    static TextView dateandtime;
    static TextView summary;
    static TextView humidity;
    private TextView nameTxt;
    static Button button2;
    String lat;
    String log;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_weekly, container, false);

        sharedPref = rootView.getContext().getSharedPreferences(AppConstatnts.SHARED_PREF, Context.MODE_PRIVATE);
        recyclerView = rootView.findViewById(R.id.recView);
        lat = sharedPref.getString(AppConstatnts.LAT_CURRENT, "Flutra");
        log = sharedPref.getString(AppConstatnts.LOG_CURRENT, "FlutraLog");
        String nameLF = sharedPref.getString(AppConstatnts.CITY_CURRENT, "London");
        String currentCity = sharedPref.getString(AppConstatnts.CITY_CURRENT, "FlutraCity");


        temperatureTxt=rootView.findViewById(R.id.temp_txt_right);
        dateandtime=rootView.findViewById(R.id.date_time_txt);
        summary=rootView.findViewById(R.id.summery_txt);
        humidity=rootView.findViewById(R.id.humidity_txt);
        currentImage=rootView.findViewById(R.id.image_img);
        button2 = rootView.findViewById(R.id.button2);
        nameTxt = rootView.findViewById(R.id.name_txt);

        nameTxt.setText(nameLF);
        System.out.println(lat);
        System.out.println(log);
        System.out.println(currentCity);


        getCurrentWeather(AppConstatnts.APPI_ID,lat,log);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), CityMap.class);
                Bundle b = new Bundle();
                Coord selectedCord = new Coord();
                selectedCord.setLat(Float.parseFloat(lat));
                selectedCord.setLon(Float.parseFloat(log));
                b.putParcelable("coord",selectedCord);
                intent.putExtra("bn",b);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void getCurrentWeather(String appid, String lat, String log) {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstatnts.SECOND_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CurrentResponse> usersE = apiInterface.getDailyWeather(appid, lat, log);

        usersE.enqueue(new Callback<CurrentResponse>() {
            @Override
            public void onResponse(Call<CurrentResponse> call, Response<CurrentResponse> response) {

                if (response.isSuccessful()) {
                    CurrentResponse currentResponse = response.body();
                    Log.d("BONIIIIIIIIII",currentResponse.getTimezone() + " Flutura");
                    initRecyclerView(currentResponse.getDaily().getData());
                    datum_s = currentResponse.getDaily().getData();

                }else
                {
                    Log.d("bani pak"," sdi");
                    try {
                        Log.d("bani pak",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CurrentResponse> call, Throwable t) {
                System.out.println("NOEXIS: ");
                t.printStackTrace();
                System.out.println("CAll onFailure");
                Log.d("ZBONI","ZBONI Flutura");
            }


        });
    }

    public static void detailClicked(int position){
        if(position < 0)
            return;

        DateFormater dateFormater = new DateFormater();
        Datum_ datumF = datum_s.get(position);
        double temp = ((datumF.getTemperatureHigh()-32)*5)/9;
        temperatureTxt.setText((int)temp + " °C");
        dateandtime.setText(dateFormater.formatLong(datumF.getTime()));
        summary.setText(datumF.getSummary());
        humidity.setText(datumF.getHumidity() + " %");
        String iconString = datumF.getIcon();
        if(iconString.contains("clear-day"))
        {
            currentImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.sunny));
        }else if(iconString.contains("clear-night"))
        {
            currentImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.moon));
        }else if(iconString.contains("rain"))
        {
            currentImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.drop));
        }else if(iconString.contains("snow"))
        {
            currentImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.snowflake));
        }else if(iconString.contains("sleet"))
        {
            currentImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.drop));
        }else if(iconString.contains("wind"))
        {
            currentImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.wind));
        }
        else
            currentImage.setImageDrawable(rootView.getResources().getDrawable(R.drawable.clouds));





    }

    private void initRecyclerView(ArrayList<Datum_> arrayList) {

        recyclerView = rootView.findViewById(R.id.recView);

        adapter = new RecViewAdapter(rootView.getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.refreshDrawableState();
    }

}
