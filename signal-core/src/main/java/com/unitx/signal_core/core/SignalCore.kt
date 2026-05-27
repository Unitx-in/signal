package com.unitx.signal_core.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class SignalCore(app: Application) : Application.ActivityLifecycleCallbacks {

    private var foregroundActivity: Activity? = null

    init {
        app.registerActivityLifecycleCallbacks(this)
        android.util.Log.d("Signal", "ActivityLifecycleCallbacks registered on: $app")
    }

    fun current(): Activity? = foregroundActivity

    override fun onActivityResumed(activity: Activity) {
        android.util.Log.d("Signal", "onActivityResumed: $activity")
        foregroundActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        if (foregroundActivity == activity) foregroundActivity = null
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}