package com.bengalitutorial.ytvideodownloder.animutil;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import androidx.recyclerview.widget.RecyclerView;

public class AnimatorUtil {

    public static void AnimatorU(RecyclerView.ViewHolder holder,boolean scrollUp){

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.itemView,

                "translationX",scrollUp==true?572:-572,0);

        objectAnimator.setDuration(1000);//1 second
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }
}
