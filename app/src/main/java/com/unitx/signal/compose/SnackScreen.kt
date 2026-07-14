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
import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.contract.type.SnackType
import com.unitx.signal_core.helper.findActivity
import com.unitx.signal_core.main.Signal

@Composable
fun SnackScreen() {
    val context = LocalContext.current
    val activity = requireNotNull(context.findActivity()) { "SnackScreen must be hosted in an Activity" }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.snack(activity, "Item saved successfully") { type = SnackType.Success }
        }) { Text("Success Snack") }

        Button(onClick = {
            Signal.snack(activity, "File uploaded") {
                type = SnackType.Error
                showCancelAction = false
                tag = "fileUploaded"
                action("Cancel") { Log.i("Snackbar", "View Action Pressed") }
            }
        }) { Text("Error Snack") }

        Button(onClick = {
            Signal.snack(activity, "First message") { type = SnackType.Info }
            Signal.snack(activity, "Second message") {
                type = SnackType.Warning
                position = SnackPosition.Bottom
                action("New") { Log.i("Snackbar", "New Action Pressed") }
            }
            Signal.snack(activity, "Third message") { type = SnackType.Success }
        }) { Text("Test Queue") }
    }
}