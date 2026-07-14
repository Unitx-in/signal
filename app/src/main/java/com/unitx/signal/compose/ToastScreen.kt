package com.unitx.signal.compose

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
import com.unitx.signal_core.contract.position.IconPosition
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.contract.type.ToastType
import com.unitx.signal_core.helper.findActivity
import com.unitx.signal_core.main.Signal

@Composable
fun ToastScreen() {
    val context = LocalContext.current
    val activity = requireNotNull(context.findActivity()) { "ToastScreen must be hosted in an Activity" }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Companion.CenterVertically),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.toast(activity, "Item saved successfully") { type = ToastType.Success }
        }) { Text("Success Toast") }

        Button(onClick = {
            Signal.toast(activity, "Something went wrong") {
                type = ToastType.Error
            }
        }) { Text("Error Toast") }

        Button(onClick = {
            Signal.toast(activity, "Low storage warning") {
                tag = "lowStorage"
                type = ToastType.Warning
                position = ToastPosition.Top
            }
        }) { Text("Warning Top Toast") }

        Button(onClick = {
            Signal.toast(activity, "Update available") {
                type = ToastType.Info
                position = ToastPosition.Center
            }
        }) { Text("Info Center Toast") }

        Button(onClick = {
            Signal.toast(activity, "File deleted") {
                type = ToastType.Error
                position = ToastPosition.Top
                iconRes = R.drawable.ic_snack_close
                iconPosition = IconPosition.Start
            }
        }) { Text("Toast with Icon") }

        Button(onClick = {
            Signal.toast(activity, "Firstf")
            Signal.toast(activity, "Second") { type = ToastType.Success }
            Signal.toast(activity, "Third") { type = ToastType.Error }
        }) { Text("Rapid Toasts") }
    }
}