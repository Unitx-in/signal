package com.unitx.signal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unitx.signal.ui.theme.SignalTheme
import com.unitx.signal_core.contract.type.DialogType
import com.unitx.signal_core.contract.position.IconPosition
import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.contract.type.SnackType
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.contract.type.ToastType
import com.unitx.signal_core.main.Signal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        SignalTestScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun DialogScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.dialog {
                title = "Successful purchase!"
                message = "Your order has been placed and will arrive in 3-5 business days."
                type = DialogType.Positive
                positive("Got it") { Log.i("Dialog", "Confirmed") }
            }
        }) { Text("Success Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "Delete file?"
                message = "This action cannot be undone. The file will be permanently removed."
                type = DialogType.Error
                positive("Delete") { Log.i("Dialog", "Deleted") }
                negative("Cancel")
            }
        }) { Text("Error Dialog with Actions") }

        Button(onClick = {
            Signal.dialog {
                title = "Low storage"
                message = "You are running low on storage. Consider clearing some space."
                type = DialogType.Action
                cancelable = true
                positive("Clear now") { Log.i("Dialog", "Clear pressed") }
                negative("Dismiss")
            }
        }) { Text("Warning cancelable") }

        Button(onClick = {
            Signal.dialog {
                title = "Update available"
                message = "A new version is available. Update now to get the latest features."
                type = DialogType.Default
                positive("Update") { Log.i("Dialog", "Update pressed") }
                negative("Later")
            }
        }) { Text("Info Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "Session expiring"
                message = "Your session will expire in 2 minutes."
                type = DialogType.Action
                autoDismiss = true
                autoDismissDuration = 3000L
                positive("Stay logged in") { Log.i("Dialog", "Session extended") }
            }
        }) { Text("Auto Dismiss Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "First dialog"
                message = "This is the first in queue."
                type = DialogType.Default
                positive("Next") { Log.i("Dialog", "First confirmed") }
            }
            Signal.dialog {
                title = "Second dialog"
                message = "This appears after the first is dismissed."
                type = DialogType.Positive
                positive("Done") { Log.i("Dialog", "Second confirmed") }
            }
        }) { Text("Test Queue") }
    }
}

@Composable
fun SnackScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.snack("Item saved successfully") { type = SnackType.Success }
        }) { Text("Success Snack") }

        Button(onClick = {
            Signal.snack("File uploaded") {
                type = SnackType.Error
                showCancelAction = false
                action("Cancel") { Log.i("Snackbar", "View Action Pressed") }
            }
        }) { Text("Error Snack") }

        Button(onClick = {
            Signal.snack("First message") { type = SnackType.Info }
            Signal.snack("Second message") {
                type = SnackType.Warning
                position = SnackPosition.Bottom
                action("New"){ Log.i("Snackbar", "New Action Pressed")}
            }
            Signal.snack("Third message") { type = SnackType.Success }
        }) { Text("Test Queue") }
    }
}

@Composable
fun ToastScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.toast("Item saved successfully") { type = ToastType.Success }
        }) { Text("Success Toast") }

        Button(onClick = {
            Signal.toast("Something went wrong") {
                type = ToastType.Error
            }
        }) { Text("Error Toast") }

        Button(onClick = {
            Signal.toast("Low storage warning") {
                type = ToastType.Warning
                position = ToastPosition.Top
            }
        }) { Text("Warning Top Toast") }

        Button(onClick = {
            Signal.toast("Update available") {
                type = ToastType.Info
                position = ToastPosition.Center
            }
        }) { Text("Info Center Toast") }

        Button(onClick = {
            Signal.toast("File deleted") {
                type = ToastType.Error
                position = ToastPosition.Top
                iconRes = com.unitx.signal_core.R.drawable.ic_snack_close
                iconPosition = IconPosition.Start
            }
        }) { Text("Toast with Icon") }

        Button(onClick = {
            Signal.toast("First")
            Signal.toast("Second") { type = ToastType.Success }
            Signal.toast("Third") { type = ToastType.Error }
        }) { Text("Rapid Toasts") }
    }
}

@Composable
fun SignalTestScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SectionLabel("Snackbar")

        Button(onClick = {
            Signal.snack("Your file has been saved successfully.") {
                type = SnackType.Success
            }
        }) { Text("Success Snack") }

        Button(onClick = {
            Signal.snack("File upload failed. Please try again.") {
                type = SnackType.Error
                showCancelAction = false
                action("Retry") { Log.i("Snack", "Retry pressed") }
            }
        }) { Text("Error Snack with Action") }

        Button(onClick = {
            Signal.snack("First: Info message incoming.") { type = SnackType.Info }
            Signal.snack("Second: Warning — disk space is low.") { type = SnackType.Warning }
            Signal.snack("Third: All uploads completed.") { type = SnackType.Success }
        }) { Text("Snack Queue (3 messages)") }

        SectionLabel("Toast")

        Button(onClick = {
            Signal.toast("Changes saved successfully.") { type = ToastType.Success }
        }) { Text("Success Toast") }

        Button(onClick = {
            Signal.toast("Something went wrong. Please try again.") {
                type = ToastType.Error
                position = ToastPosition.Top
            }
        }) { Text("Error Toast at Top") }

        Button(onClick = {
            Signal.toast("First: Task started.") { type = ToastType.Info }
            Signal.toast("Second: Task is in progress.") { type = ToastType.Warning }
            Signal.toast("Third: Task completed successfully.") { type = ToastType.Success }
        }) { Text("Toast Queue (3 messages)") }

        SectionLabel("Dialog")

        Button(onClick = {
            Signal.dialog {
                title = "Purchase Successful"
                message = "Your order has been placed and will arrive within 3 to 5 business days. Thank you for shopping with us."
                type = DialogType.Positive
                positive("Got it") { Log.i("Dialog", "Purchase confirmed") }
            }
        }) { Text("Success Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "Delete File?"
                message = "This action is permanent and cannot be undone. The file will be removed from all your devices."
                type = DialogType.Error
                cancelable = true
                positive("Delete") { Log.i("Dialog", "File deleted") }
                negative("Cancel")
            }
        }) { Text("Destructive Dialog with Actions") }

        Button(onClick = {
            Signal.dialog {
                title = "First in Queue"
                message = "This is the first dialog. Dismiss it by tapping 'Next' to see the second dialog appear automatically."
                type = DialogType.Default
                positive("Next") {}
            }
            Signal.dialog {
                title = "Second in Queue"
                message = "This is the second dialog. It was waiting while the first was showing. The queue works correctly."
                type = DialogType.Positive
                positive("Done") {}
            }
        }) { Text("Dialog Queue (2 dialogs)") }

        Button(onClick = {
            Signal.snack("Snack is running independently on its own queue.") { type = SnackType.Info }
            Signal.toast("Toast fired at the same time as the snack and dialog.") { type = ToastType.Warning }
            Signal.dialog {
                title = "All Three Fired!"
                message = "A snack, a toast, and this dialog were all triggered simultaneously. Each runs on its own independent queue without interference."
                type = DialogType.Positive
                positive("Great") {}
            }
        }) { Text("🔥 Fire All Three Together") }
    }
}

@Composable
private fun SectionLabel(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}