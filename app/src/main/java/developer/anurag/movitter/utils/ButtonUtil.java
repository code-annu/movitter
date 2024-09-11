package developer.anurag.movitter.utils;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import developer.anurag.movitter.R;

public class ButtonUtil {
    @SuppressLint("ClickableViewAccessibility")
    public static Button applyPushEffect(Button button){
        button.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Start scale down animation
                    Animation scaleDown = AnimationUtils.loadAnimation(button.getContext(), R.anim.scale_down);
                    v.startAnimation(scaleDown);
                    return false;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // Start scale up animation
                    Animation scaleUp = AnimationUtils.loadAnimation(button.getContext(), R.anim.scale_up);
                    v.startAnimation(scaleUp);
                    return false;
            }
            return false;
        });

        return button;
    }
}
