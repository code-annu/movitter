package developer.anurag.movitter.utils;

import android.content.Context;

public class DeviceUtil {
    public static int getDeviceWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int convertDpToPx(Context context,float dp){
        float density=context.getResources().getDisplayMetrics().density;
        return Math.round(dp*density);
    }
}
