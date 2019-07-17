package com.flutra.augmentedreality.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormater {

    SimpleDateFormat sdfDateS;

    public DateFormater() {
        sdfDateS = new SimpleDateFormat("E dd MMM yyyy", Locale.getDefault());
    }

    public String formatLong(long d) {
        String formatedDate = "";
        //Log.d("STring",d);
        formatedDate = sdfDateS.format(new Date(d));
        return formatedDate;
    }

    public String format(long d) {
        String formatedDate = "";
        Date date = new Date(d * 1000L); // *1000 is to convert seconds to milliseconds
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println(day+" day");
        switch (day) {
            case 1:
                formatedDate = "Monday";
                break;
            case 2:
                formatedDate = "Tuesday";
                break;
            case 3:
                formatedDate = "Wednesday";
                break;
            case 4:
                formatedDate = "Thursday";
                break;
            case 5:
                formatedDate = "Friday";
                break;
            case 6:
                formatedDate = "Saturday";
                break;
            case 7:
                formatedDate = "Sunday";
                break;
        }
        return formatedDate;
    }
}
