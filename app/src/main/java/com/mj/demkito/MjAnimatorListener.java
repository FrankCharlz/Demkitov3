package com.mj.demkito;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.widget.ImageView;

public class MjAnimatorListener implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private final float imageViewWidth;
    private Matrix matrix;
    private float scaleFactor;
    private ImageView imageView;
    private int ANIMATION_DIRECTION = -1;

    public MjAnimatorListener(Matrix matrix, ImageView imageView, float scaleFactor) {
        this.imageView = imageView;
        this.imageViewWidth = imageView.getWidth();
        this.scaleFactor = scaleFactor;
        this.matrix = matrix;
    }


    float value;
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        value = (Float) animation.getAnimatedValue();
        value = (ANIMATION_DIRECTION == -1) ? value : value - imageViewWidth;
        matrix.reset();
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate(ANIMATION_DIRECTION*value, 0);
        imageView.setImageMatrix(matrix);
    }

    //0716586714

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        ANIMATION_DIRECTION *= -1;
        animation.setStartDelay(300);
        animation.start();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

