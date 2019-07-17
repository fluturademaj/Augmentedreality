package com.flutra.augmentedreality;

import com.flutra.augmentedreality.models.CurrentResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {



    @GET("weather")
    Call<WeatherResponse> getWeatherData(@Query("q") String q, @Query("appid") String appid);

    @GET()
    Call<WeatherResponse> getWeatherLatLon(@Query("lat") double latit,
                                           @Query("lon") double lon,
                                           @Query("appid") String apiKey);



    @Headers({
            "cache-control: no-cache",
            "Content-Type: application/json",
            "Accept: application/json"

    }
    )
    @GET("forecast/{appid}/{lat},{log}")
    Call<CurrentResponse> getDailyWeather(@Path("appid") String appid, @Path("lat") String lat,
                                          @Path("log") String log);
}
