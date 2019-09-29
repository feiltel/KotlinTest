package com.nut2014.kotlintest.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * @author feiltel 2019/9/29 0029
 */
class AnimatorUtils {


    private var animator: ObjectAnimator? = null


    fun startRotation(view: View) {
        animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        animator!!.interpolator = LinearInterpolator()
        animator!!.duration = 2000
        animator!!.repeatCount = -1
        animator!!.start()
    }

    fun stopRotation() {
        animator!!.end()
    }
}
