package com.mj.demkito;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class BackgroundAnimation {

    private static final long ANIMATION_DURATION = 15000;
    private final ImageView imageview;
    private final Drawable drawable;
    private final float scaleFactor;
    private final Matrix matrix = new Matrix();
    protected ValueAnimator mAnimator;


    public BackgroundAnimation(ImageView imageview) {
        this.imageview = imageview;
        this.drawable = imageview.getDrawable();

        //used to scale height to match parent theme... with matrix
        scaleFactor = (float)imageview.getHeight() / (float)drawable.getIntrinsicHeight();
        matrix.postScale(scaleFactor, scaleFactor);

    }


    public void start() {

        imageview.setImageDrawable(drawable);
        imageview.setImageMatrix(matrix);

        //animate throught out the width of the image view --wrap--content...
       mAnimator = ValueAnimator.ofFloat(0, imageview.getWidth());

        //mAnimator.setInterpolator(new LinearInterpolator());
        MjAnimatorListener mAnimationListener = new MjAnimatorListener();
        mAnimator.addUpdateListener(mAnimationListener);
        mAnimator.addListener(mAnimationListener);
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.start();

    }

    class MjAnimatorListener implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
        private int ANIMATION_DIRECTION = -1;
        float imageViewWidth = imageview.getWidth();

        float value;
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            value = (Float) animation.getAnimatedValue();
            value = (ANIMATION_DIRECTION == -1) ? value : value - imageViewWidth;
            matrix.reset();
            matrix.postScale(scaleFactor, scaleFactor);
            matrix.postTranslate(ANIMATION_DIRECTION*value, 0);
            imageview.setImageMatrix(matrix);
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

    public interface AnimationControl {
        abstract void pauseAnimation(ValueAnimator valueAnimator);
        //abstract void stopAnimation(ValueAnimator valueAnimator);
    }

}
