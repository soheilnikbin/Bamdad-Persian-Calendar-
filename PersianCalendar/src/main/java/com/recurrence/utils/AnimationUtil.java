package com.recurrence.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.byagowi.persiancalendar.R;


public class AnimationUtil {
    public static void shakeView(View view, Context context) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake_view);
        view.startAnimation(shake);
    }
}