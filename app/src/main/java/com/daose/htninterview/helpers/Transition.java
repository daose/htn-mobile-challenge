package com.daose.htninterview.helpers;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;

public class Transition {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hide(View view, AnimatorListenerAdapter listener) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            listener.onAnimationEnd(null);
            return;
        }

        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, view.getWidth(), 0, initialRadius, 0);
        anim.addListener(listener);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void show(View view) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        view.setVisibility(View.VISIBLE);

        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, view.getWidth(), 0, 0, finalRadius);
        anim.start();
    }
}
