package com.unitx.signal_core.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object SignalImageLoader {
    private val memoryCache = LruCache<String, Bitmap>(
        (Runtime.getRuntime().maxMemory() / 8 / 1024).toInt()
    )
    private val client = OkHttpClient()

    fun load(url: String, into: ImageView, placeholder: Drawable? = null, requestTag: String = url) {
        into.tag = requestTag

        memoryCache.get(url)?.let {
            if (into.tag == requestTag) into.setImageBitmap(it)
            return
        }
        placeholder?.let { into.setImageDrawable(it) }

        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = fetchBitmap(url)
            withContext(Dispatchers.Main) {
                if (into.tag != requestTag) return@withContext // stale — a newer bind happened
                bitmap?.let {
                    memoryCache.put(url, it)
                    into.setImageBitmap(it)
                }
            }
        }
    }

    private fun fetchBitmap(url: String): Bitmap? = runCatching {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            response.body?.byteStream()?.let { BitmapFactory.decodeStream(it) }
        }
    }.getOrNull()
}