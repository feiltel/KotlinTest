package com.nut2014.baselibrary.anim;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author feiltel 2019/9/30 0030
 */
public class RotationAnim {


    private ObjectAnimator animator;

    public RotationAnim(View view) {
        this.animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000);
        animator.setRepeatCount(-1);
    }

    public void start() {
        assert animator != null;
        System.out.println(animator.isRunning() + ">>" + animator.isPaused() + ">>>" + animator.isStarted() + ">>");
        if (animator.isPaused()) {
            animator.resume();
        } else {
            animator.start();
        }
    }

    public void pause() {
        if (animator.isRunning()) {
            animator.pause();
        }
    }

    public ObjectAnimator getAnimator() {
        return animator;
    }

    public void end() {
        if (animator.isStarted()) {
            animator.end();
        }
    }
}
