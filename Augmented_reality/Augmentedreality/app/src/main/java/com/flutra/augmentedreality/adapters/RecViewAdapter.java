package com.flutra.augmentedreality.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.flutra.augmentedreality.R;
import com.flutra.augmentedreality.fragment.WeeklyWeatherFragment;
import com.flutra.augmentedreality.models.Datum_;
import com.flutra.augmentedreality.utils.DateFormater;

import java.util.ArrayList;

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ViewHolder> {
    private static final String TAG = "NotifReciewAdapter";
    private ArrayList<Datum_> allDays = new ArrayList<>();
    private Context mContext;


    public RecViewAdapter(Context context, ArrayList<Datum_> allDays) {
        this.mContext = context;
        this.allDays = allDays;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        DateFormater dateFormater = new DateFormater();

        holder.dayTxt.setText(dateFormater.format(allDays.get(position).getTime()));
        double temp = ((allDays.get(position).getTemperatureHigh()-32)*5)/9;
        holder.temperatureTxt.setText((int)temp + " °C");

        String iconString = allDays.get(position).getIcon();

        if(iconString.contains("clear-day"))
        {
            holder.weatherImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sunny));
        }else if(iconString.contains("clear-night"))
        {
            holder.weatherImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.moon));
        }else if(iconString.contains("rain"))
        {
            holder.weatherImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drop));
        }else if(iconString.contains("snow"))
        {
            holder.weatherImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.snowflake));
        }else if(iconString.contains("sleet"))
        {
            holder.weatherImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.drop));
        }else if(iconString.contains("wind"))
        {
            holder.weatherImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.wind));
        }
        else
            holder.weatherImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.clouds));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyWeatherFragment.detailClicked(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return allDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayTxt;
        TextView temperatureTxt;
        ImageView weatherImage;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            dayTxt = itemView.findViewById(R.id.day_txt);
            temperatureTxt = itemView.findViewById(R.id.temperature_txt);
            weatherImage = itemView.findViewById(R.id.weather_img);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }


    }
}
