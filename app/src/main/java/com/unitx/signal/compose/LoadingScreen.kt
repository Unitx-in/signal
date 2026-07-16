package com.unitx.signal.compose

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.unitx.signal_core.R
import com.unitx.signal_core.contract.type.LoadingType
import com.unitx.signal_core.helper.findActivity
import com.unitx.signal_core.main.Signal

@Composable
fun LoadingScreen() {
    val context = LocalContext.current
    val activity = requireNotNull(context.findActivity()) { "LoadingScreen must be hosted in an Activity" }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Companion.CenterVertically),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.loading(activity) {
                title = "Please wait a moment."
                subtitle = "We are processing your request."
            }
        }) { Text("Indefinite Loading") }

        Button(onClick = {
            Signal.loading(activity) {
                title = "Downloading files"
                subtitle = "This may take a few seconds."
                type = LoadingType.Determinate
                progress = 0
                progressMessage = "Starting download..."
                icon = R.drawable.ic_download
                iconUrl = "https://picsum.photos/200/300.jpg"
            }
            // simulate progress
            var p = 0
            val handler = Handler(Looper.getMainLooper())
            fun tick() {
                if (p >= 100) {
                    Signal.dismissLoading(); return
                }
                p += 10
                Signal.updateProgress(
                    p,
                    if (p == 100) "Download complete!" else "Downloading files..."
                )
                handler.postDelayed(::tick, 2000)
            }
            handler.postDelayed(::tick, 500)
        }) { Text("Determinate Loading") }

        Button(onClick = {
            Signal.loading(activity) {
                title = "Analyzing data"
                subtitle = "This may take a few seconds."
                cancelable = true
                onCancelled = { Log.i("Loading", "User cancelled") }
            }
        }) { Text("Cancelable Loading") }

        Button(onClick = {
            Signal.loading(activity) {
                title = "Please wait"
                dismissOnBackPress = true
                onDismissed = { Log.i("Loading", "Dismissed") }
            }
        }) { Text("Dismiss on back press") }

        Button(onClick = {
            Signal.loading(activity) {
                title = "Processing payment"
                subtitle = "Do not close the app."
                icon = R.drawable.ic_signal_success
            }
        }) { Text("Loading with Icon") }

        Button(onClick = {
            Signal.loading(activity) { simpleLoading = true; dismissOnBackPress = true }
        }) { Text("Simple Loading") }
    }
}