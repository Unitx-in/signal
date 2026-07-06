package com.unitx.signal_core.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

internal class ActivityProvider(app: Application) : Application.ActivityLifecycleCallbacks {

    private var foregroundActivity: WeakReference<Activity>? = null

    // Listeners scoped to one specific Activity instance. Android guarantees the same
    // Activity object is passed to every lifecycle callback for that instance's entire
    // life (onCreate through onDestroy), so using it as a map key is a reliable identity
    // — no manual == / === comparisons needed anywhere else in the library.
    private val destroyListeners = mutableMapOf<Activity, MutableList<() -> Unit>>()

    init {
        app.registerActivityLifecycleCallbacks(this)
    }

    fun current(): Activity? = foregroundActivity?.get()

    /**
     * Registers [listener] to run only when the CURRENT foreground activity is destroyed,
     * and returns a token to unregister early. Returns null if there is no foreground
     * activity right now (caller should treat this as "cannot attach").
     *
     * This is the only supported way for a handler to bind to activity lifecycle —
     * it should never call [addOnDestroyListener] directly.
     */
    fun bindToCurrentActivity(listener: () -> Unit): ActivityBinding? {
        val activity = current() ?: return null
        addOnDestroyListener(activity, listener)
        return ActivityBinding(this, activity, listener)
    }

    private fun addOnDestroyListener(activity: Activity, listener: () -> Unit) {
        destroyListeners.getOrPut(activity) { mutableListOf() }.add(listener)
    }

    internal fun removeOnDestroyListener(activity: Activity, listener: () -> Unit) {
        val list = destroyListeners[activity] ?: return
        list.remove(listener)
        if (list.isEmpty()) destroyListeners.remove(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        foregroundActivity = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {
        if (foregroundActivity?.get() == activity) foregroundActivity = null
        destroyListeners.remove(activity)?.toList()?.forEach { it() }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}