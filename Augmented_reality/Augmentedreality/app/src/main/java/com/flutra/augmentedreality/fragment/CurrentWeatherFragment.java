package com.flutra.augmentedreality.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;




import com.flutra.augmentedreality.ApiInterface;
import com.flutra.augmentedreality.Coord;
import com.flutra.augmentedreality.R;
import com.flutra.augmentedreality.WeatherResponse;
import com.flutra.augmentedreality.constants.AppConstatnts;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CurrentWeatherFragment extends Fragment {

    private final static String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private final static String API_KEY = "4985cef944555b5dd6bd6496f8f519c7";

    private Coord selectedCord;
    private Button button;
    private TextView temperature;
    private TextView temp_max;
    private TextView temp_min;
    private TextView cityView;
    private TextView humidity;
    private TextView pressure;
    private EditText cityInput;
    private ProgressBar progressBar;
    private ImageView imageView;
    private View rootView;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_current, container, false);

        button = rootView.findViewById(R.id.button);
        temperature = rootView.findViewById(R.id.tVtemperature);
        temp_max = rootView.findViewById(R.id.tVtempMax);
        temp_min = rootView.findViewById(R.id.tVtempMin);
        cityView = rootView.findViewById(R.id.textView2);
        humidity = rootView.findViewById(R.id.tVpressure);
        pressure = rootView.findViewById(R.id.tVhumidity);
        cityInput = rootView.findViewById(R.id.editText);
        progressBar = rootView.findViewById(R.id.progressBar);
        imageView = rootView.findViewById(R.id.imageView);
        sharedPref = rootView.getContext().getSharedPreferences(AppConstatnts.SHARED_PREF, Context.MODE_PRIVATE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputText = cityInput.getText().toString();

                if(!inputText.isEmpty()){
                    getWeatherDataFromServer(inputText, API_KEY);
                }else{
                    Toast.makeText(rootView.getContext(), "Type a city please", Toast.LENGTH_LONG).show();
                }
            }
        });


        return rootView;
    }


    public void getWeatherDataFromServer(final String city, String appid){

        ApiInterface apiInterface = this.getInterface();
        Call<WeatherResponse> mService = apiInterface.getWeatherData(city, appid);

        Log.i("ser", mService.request() + "");
        progressBar.setVisibility(View.VISIBLE);
        mService.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {

                WeatherResponse weatherResponse = response.body();
                progressBar.setVisibility(View.INVISIBLE);
                if(weatherResponse != null){
                    Log.i("status", "if" );
                    Log.i("Res", weatherResponse.getMain().getTemp() + " ");
                    Log.i("Res", weatherResponse.getMain().getHumidity() + " ");

                    String icon = weatherResponse.getWeather().get(0).getIcon();
                    String stringWeather = weatherResponse.getWeather().get(0).toString();
                    //System.out.println(stringWeather+" FLUTURA");
                    String iconLink = "https://openweathermap.org/img/w/" + icon + ".png";


                    Picasso.get().load(iconLink).into(imageView);
                    Log.i("WeatherIcon", iconLink);

                    Float _temperature = weatherResponse.getMain().getTemp() - 273.15f;
                    temperature.setText(String.format("%.2f", _temperature)+ "°C");

                    Float _temperatureMax = weatherResponse.getMain().getTemp_max() - 273.15f;
                    temp_max.setText(_temperatureMax + "°C");

                    Float _temperatureMin = weatherResponse.getMain().getTemp_min() - 273.15f;
                    temp_min.setText(_temperatureMin + "°C");

                    cityView.setText(weatherResponse.getName());

                    Integer _pressure = weatherResponse.getMain().getPressure();
                    pressure.setText(_pressure  + " %");

                    Integer _humidity = weatherResponse.getMain().getHumidity();
                    humidity.setText(_humidity + " hPa");
                    selectedCord = weatherResponse.getCoord();

                    addDataToStorage(AppConstatnts.LAT_CURRENT,weatherResponse.getCoord().getLat()+"");
                    addDataToStorage(AppConstatnts.LOG_CURRENT,weatherResponse.getCoord().getLon()+"");
                    addDataToStorage(AppConstatnts.CITY_CURRENT,city);


                }else{
                    Log.i("status", " else");
                    Toast.makeText(rootView.getContext(), "We couldn't find a city with name: " + cityInput.getText(), Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.i("failure response", t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                call.cancel();
            }
        });
    }


    public ApiInterface getInterface(){

        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;
    }

    public void addDataToStorage(String key, String value)
    {
        if(key == null || key.isEmpty() || value == null || value.isEmpty())
            return;

        sharedPref.edit().putString(key,value).apply();
    }

}
