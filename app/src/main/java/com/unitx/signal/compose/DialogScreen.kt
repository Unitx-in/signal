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
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.contract.type.DialogType
import com.unitx.signal_core.contract.type.ToastType
import com.unitx.signal_core.helper.findActivity
import com.unitx.signal_core.main.Signal

@Composable
fun DialogScreen() {
    val context = LocalContext.current
    // Non-null here is safe: this Composable only ever runs while hosted inside an
    // Activity (set via setContent). If findActivity() ever returns null, something
    // is fundamentally wrong with how this screen is hosted, so failing loudly here
    // is preferable to silently swallowing every button tap.
    val activity = requireNotNull(context.findActivity()) { "DialogScreen must be hosted in an Activity" }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Companion.CenterVertically),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Successful purchase!"
                message = "Your order has been placed and will arrive in 3-5 business days."
                type = DialogType.Positive
                dismissOnPositive = false
                positive("Got it") {
                    Signal.toast(activity, "Working") {
                        type = ToastType.Success
                        position = ToastPosition.Bottom
                        onDismissed = { Signal.dismissDialog() }
                    }
                }
            }
        }) { Text("Success Dialog") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Delete file?"
                message = "This action cannot be undone. The file will be permanently removed."
                type = DialogType.Error
                positive("Delete") { Log.i("Dialog", "Deleted") }
                negative("Cancel")
                neutral("Know more") { Log.i("Dialog", "Neutral") }
            }
        }) { Text("Error Dialog with Actions") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Low storage"
                message = "You are running low on storage. Consider clearing some space."
                type = DialogType.Action
                cancelable = true
                dismissOnNegative = false
                dismissOnPositive = false
                positive("Clear now") { Log.i("Dialog", "Clear pressed") }
                negative("Dismiss") { Signal.dismissDialog() }
            }
        }) { Text("Warning cancelable") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Update available"
                message = "A new version is available. Update now to get the latest features."
                type = DialogType.Default
                positive("Update") { Log.i("Dialog", "Update pressed") }
                negative("Later")
            }
        }) { Text("Info Dialog") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Session expiring"
                message = "Your session will expire in 2 minutes."
                type = DialogType.Action
                autoDismiss = true
                autoDismissDuration = 3000L
                positive("Stay logged in") { Log.i("Dialog", "Session extended") }
            }
        }) { Text("Auto Dismiss Dialog") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "First dialog"
                message = "This is the first in queue."
                type = DialogType.Default
                positive("Next") { Log.i("Dialog", "First confirmed") }
            }
            Signal.dialog(activity) {
                title = "Second dialog"
                message = "This appears after the first is dismissed."
                type = DialogType.Positive
                positive("Done") { Log.i("Dialog", "Second confirmed") }
            }
        }) { Text("Test Queue") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Terms & Conditions"
                message =
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                type = DialogType.Default
                positive("Accept") { Log.i("Dialog", "Accepted") }
                negative("Decline")
            }
        }) { Text("Scrollable Message") }
    }
}