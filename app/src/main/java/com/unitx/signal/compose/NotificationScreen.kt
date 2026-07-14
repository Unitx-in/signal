package com.unitx.signal.compose

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
import com.unitx.signal_core.contract.position.NotificationPosition
import com.unitx.signal_core.helper.findActivity
import com.unitx.signal_core.main.Signal

@Composable
fun NotificationScreen() {
    val context = LocalContext.current
    val activity = requireNotNull(context.findActivity()) { "NotificationScreen must be hosted in an Activity" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.notif(activity) {
                message = "Saved to"
                highlight = "Men fashion casual outfits"
            }
        }) { Text("Basic Notif") }

        Button(onClick = {
            Signal.notif(activity) {
                message = "Added to"
                highlight = "Favorites"
                position = NotificationPosition.Bottom
            }
        }) { Text("Bottom Notif (tagged)") }

        Button(onClick = {
            Signal.notif(activity) {
                message = "Shared with"
                highlight = "Design Team"
                position = NotificationPosition.Center
                dismissOnBackPress = true
                onDismissed = { Log.i("Notification", "Notif dismissed") }
            }
        }) { Text("Center Notif (back press)") }
    }
}