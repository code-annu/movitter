package developer.anurag.movitter.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

public class ShowHideUtil {
    public static int VERY_SHOR_DURATION=100;
    public static int SHORT_DURATION=200;
    public static int MEDIUM_DURATION=300;
    public static int LONG_DURATION=500;
    public static int VERY_LONG_DURATION=1000;

    public static void showViewWithFadeIn(View view,int duration){
        if(view.getVisibility()==View.GONE){
            view.setVisibility(View.VISIBLE);
            ObjectAnimator animator=ObjectAnimator.ofFloat(view,"alpha",0f,1f);
            animator.setDuration(duration);
            animator.start();
        }
    }

    public static void hideViewWithFadeOut(View view,int duration){
        if(view.getVisibility()==View.VISIBLE){
            ObjectAnimator animator=ObjectAnimator.ofFloat(view,"alpha",1f,0f);
            animator.setDuration(duration);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
            animator.start();
        }
    }
}
