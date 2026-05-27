package com.unitx.signal_core.provider

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

class ActivityProvider(app: Application) : Application.ActivityLifecycleCallbacks {

    private var foregroundActivity: WeakReference<Activity>? = null
    private val destroyListeners = mutableListOf<(Activity) -> Unit>()

    init {
        app.registerActivityLifecycleCallbacks(this)
    }

    fun current(): Activity? = foregroundActivity?.get()

    fun addOnDestroyListener(listener: (Activity) -> Unit) {
        destroyListeners.add(listener)
    }

    fun removeOnDestroyListener(listener: (Activity) -> Unit) {
        destroyListeners.remove(listener)
    }

    override fun onActivityResumed(activity: Activity) {
        foregroundActivity = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (foregroundActivity?.get() == activity) foregroundActivity = null
    }

    override fun onActivityDestroyed(activity: Activity) {
        destroyListeners.forEach { it(activity) }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}