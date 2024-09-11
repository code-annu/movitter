package developer.anurag.movitter.utils;

import android.annotation.SuppressLint;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {
    @SuppressLint("DefaultLocale")
    public static String getUploadTime(long milliseconds){
        long currentMillis = System.currentTimeMillis();
        long diffMillis = currentMillis - milliseconds;

        // Define time units
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
        long days = TimeUnit.MILLISECONDS.toDays(diffMillis);
        long months = days / 30;
        long years = days / 365;

        // Format output based on largest time unit
        if (years > 0) {
            return years + "y";
        } else if (months > 0) {
            return months + "m";
        } else if (days > 0) {
            return days + "d";
        } else if (hours > 0) {
            return hours + "h";
        } else if (minutes > 0) {
            return minutes + "min";
        } else {
            return seconds + "sec";
        }
    }

    public static String getGreeting(){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 5 && hourOfDay < 12) {
            return "Good Morning";
        } else if (hourOfDay >= 12 && hourOfDay < 17) {
            return "Good Afternoon";
        } else if (hourOfDay >= 17 && hourOfDay < 21) {
            return "Good Evening";
        } else {
            return "Good Night";
        }
    }
}
