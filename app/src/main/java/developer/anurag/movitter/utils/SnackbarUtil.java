package developer.anurag.movitter.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import developer.anurag.movitter.R;

public class SnackbarUtil {
    public static Snackbar makeTopSnackbar(View root,int message){
        Snackbar snackbar=Snackbar.make(root,"",Snackbar.LENGTH_SHORT);
        View view=snackbar.getView();
        @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout=(Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT);
        View customView= LayoutInflater.from(root.getContext()).inflate(R.layout.accent_snackbar_view,null);
        snackbarLayout.addView(customView,0);
        TextView textView=customView.findViewById(R.id.asv_messageTV);
        textView.setText(message);
        CoordinatorLayout.LayoutParams layoutParams=(CoordinatorLayout.LayoutParams) view.getLayoutParams();
        layoutParams.gravity= Gravity.TOP;
        layoutParams.topMargin=DeviceUtil.convertDpToPx(root.getContext(),50);
        view.setLayoutParams(layoutParams);
        return snackbar;
    }

    public static Snackbar makeTopSnackbarOnLayout(View root,int message){
        Snackbar snackbar=Snackbar.make(root,"",Snackbar.LENGTH_SHORT);
        View view=snackbar.getView();
        @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout=(Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT);
        View customView= LayoutInflater.from(root.getContext()).inflate(R.layout.accent_snackbar_view,null);
        snackbarLayout.addView(customView,0);
        TextView textView=customView.findViewById(R.id.asv_messageTV);
        textView.setText(message);
        FrameLayout.LayoutParams layoutParams=(FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.gravity= Gravity.TOP;
        layoutParams.topMargin=DeviceUtil.convertDpToPx(root.getContext(),50);
        view.setLayoutParams(layoutParams);
        return snackbar;
    }
}
