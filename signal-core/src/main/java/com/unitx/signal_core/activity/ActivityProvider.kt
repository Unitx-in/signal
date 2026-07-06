package com.unitx.signal_core.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle

internal class ActivityProvider(app: Application) : Application.ActivityLifecycleCallbacks {
    private val destroyListeners = mutableMapOf<Activity, MutableList<() -> Unit>>()

    init {
        app.registerActivityLifecycleCallbacks(this)
    }

    fun bindTo(activity: Activity, listener: () -> Unit): ActivityBinding {
        destroyListeners.getOrPut(activity) { mutableListOf() }.add(listener)
        return ActivityBinding(this, activity, listener)
    }

    internal fun removeOnDestroyListener(activity: Activity, listener: () -> Unit) {
        val list = destroyListeners[activity] ?: return
        list.remove(listener)
        if (list.isEmpty()) destroyListeners.remove(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        destroyListeners.remove(activity)?.toList()?.forEach { it() }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}