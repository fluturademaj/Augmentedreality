package com.flutra.augmentedreality.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flutra.augmentedreality.R;
import com.flutra.augmentedreality.constants.AppConstatnts;

import static android.content.Context.MODE_PRIVATE;

public class LastCityFragment extends Fragment {

    private View rootView;
    private SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_last_city, container, false);
        TextView lastCity=rootView.findViewById(R.id.last_city_txt);
        sharedPref = getActivity().getSharedPreferences(AppConstatnts.SHARED_PREF, MODE_PRIVATE);
        String currentCity = sharedPref.getString(AppConstatnts.CITY_CURRENT, "doesnotexist");
        lastCity.setText(currentCity);

        return rootView;


    }
}
