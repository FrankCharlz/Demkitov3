package com.mj.demkito;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.widget.ImageView;

public class MjAnimatorListener implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private Matrix matrix;
    private float scaleFactor;
    private ImageView imageView;
    private int ANIMATION_DIRECTION = -1;

    public MjAnimatorListener(Matrix matrix, ImageView imageView, float scaleFactor, ValueAnimator mAnimator) {
        this.imageView = imageView;
        this.scaleFactor = scaleFactor;
        this.matrix = matrix;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (Float) animation.getAnimatedValue();
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
        animation.setStartDelay(200);
       imageView.refreshDrawableState();
        animation.start();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

