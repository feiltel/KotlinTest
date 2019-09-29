package com.nut2014.kotlintest.base

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * 全局监听Activity生命周期
 */
class MyActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    lateinit var currentActivity: Activity
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}
