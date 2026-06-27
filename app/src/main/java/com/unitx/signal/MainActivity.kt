package com.unitx.signal

import android.os.Bundle
import android.text.InputType
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.unitx.signal_core.contract.type.LoadingType

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
                dismissOnPositive = false
                positive("Got it") {
                    Signal.toast("Working") {
                        type = ToastType.Success
                        position = ToastPosition.Bottom
                        onDismissed =  {
                            Signal.dismissDialog()
                        }
                    }
                }
            }
        }) { Text("Success Dialog") }

        Button(onClick = {
            Signal.dialog {
                title = "Delete file?"
                message = "This action cannot be undone. The file will be permanently removed."
                type = DialogType.Error
                positive("Delete") { Log.i("Dialog", "Deleted") }
                negative("Cancel")
                neutral("Know more") { Log.i("Dialog", "Neutral") }
            }
        }) { Text("Error Dialog with Actions") }

        Button(onClick = {
            Signal.dialog {
                title = "Low storage"
                message = "You are running low on storage. Consider clearing some space."
                type = DialogType.Action
                cancelable = true
                dismissOnNegative = false
                dismissOnPositive = false
                positive("Clear now") { Log.i("Dialog", "Clear pressed") }
                negative("Dismiss") {
                    Signal.dismissDialog()
                }
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

//        DialogTextInput()

        Button(onClick = {
            Signal.dialog {
                title = "Terms & Conditions"
                message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                type = DialogType.Default
                positive("Accept") { Log.i("Dialog", "Accepted") }
                negative("Decline")
            }
        }) { Text("Scrollable Message") }
    }
}

@Composable
fun DialogTextInput(){
    Button(onClick = {
        Signal.dialog {
            title = "Rename File"
            message = "Enter a new name for the file."
            type = DialogType.Default
            input {
                hint = "File name"
                prefill = "document_final"
                maxLength = 40
                showCounter = true
                validator = { it.isNotBlank() }
                validationError = "Name cannot be empty"
                onInput = { Log.i("Dialog", "Renamed to: $it") }
            }
            positive("Rename") {}
            negative("Cancel")
        }
    }) { Text("Input — Single Line") }

    Button(onClick = {
        Signal.dialog {
            title = "Add Note"
            message = "Describe the issue in detail."
            type = DialogType.Action
            input {
                hint = "Write your note..."
                multiLine = true
                maxLength = 200
                showCounter = true
                onInput = { Log.i("Dialog", "Note: $it") }
            }
            positive("Save") {}
            negative("Cancel")
        }
    }) { Text("Input — Multi Line") }

    Button(onClick = {
        Signal.dialog {
            title = "Change Password"
            message = "Enter your new password."
            type = DialogType.Default
            input {
                hint = "Password"
                password = true
                validator = { it.length >= 8 }
                validationError = "Must be at least 8 characters"
                onInput = { Log.i("Dialog", "Password set") }
            }
            positive("Confirm") {}
            negative("Cancel")
        }
    }) { Text("Input — Password") }

    Button(onClick = {
        Signal.dialog {
            title = "Set Limit"
            message = "Enter a numeric value between 1–999."
            type = DialogType.Action
            input {
                hint = "Amount"
                inputType = InputType.TYPE_CLASS_NUMBER
                maxLength = 3
                validator = { it.isNotEmpty() && it.toIntOrNull() != null }
                validationError = "Enter a valid number"
                onInput = { Log.i("Dialog", "Limit: $it") }
            }
            positive("Set") {}
            negative("Cancel")
        }
    }) { Text("Input — Numeric") }

    Button(onClick = {
        Signal.dialog {
            title = "Leave a Reason"
            message = "Tell us why you're leaving."
            type = DialogType.Error
            input {
                hint = "Reason"
                multiLine = true
                maxLength = 150
                showCounter = true
                validator = { it.trim().length >= 10 }
                validationError = "Please write at least 10 characters"
                onInput = { Log.i("Dialog", "Reason: $it") }
            }
            positive("Submit") {}
            negative("Skip")
        }
    }) { Text("Input — Validated Multi Line") }
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
                tag = "fileUploaded"
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
                tag = "lowStorage"
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
            Signal.toast("Firstf")
            Signal.toast("Second") { type = ToastType.Success }
            Signal.toast("Third") { type = ToastType.Error }
        }) { Text("Rapid Toasts") }
    }
}


@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.loading {
                title = "Please wait a moment."
                subtitle = "We are processing your request."
            }
        }) { Text("Indefinite Loading") }

        Button(onClick = {
            Signal.loading {
                title = "Downloading files"
                subtitle = "This may take a few seconds."
                type = LoadingType.Determinate
                progress = 0
                progressMessage = "Starting download..."
                icon = com.unitx.signal_core.R.drawable.ic_download
            }
            // simulate progress
            var p = 0
            val handler = android.os.Handler(android.os.Looper.getMainLooper())
            fun tick() {
                if (p >= 100) { Signal.dismissLoading(); return }
                p += 10
                Signal.updateProgress(p, if (p == 100) "Download complete!" else "Downloading files...")
                handler.postDelayed(::tick, 2000)
            }
            handler.postDelayed(::tick, 500)
        }) { Text("Determinate Loading") }

        Button(onClick = {
            Signal.loading {
                title = "Analyzing data"
                subtitle = "This may take a few seconds."
                cancelable = true
                onCancelled = { Log.i("Loading", "User cancelled") }
            }
        }) { Text("Cancelable Loading") }

        Button(onClick = {
            Signal.loading {
                title = "Please wait"
                dismissOnBackPress = true
                onDismissed = { Log.i("Loading", "Dismissed") }
            }
        }) { Text("Dismiss on back press") }

        Button(onClick = {
            Signal.loading {
                title = "Processing payment"
                subtitle = "Do not close the app."
                icon = com.unitx.signal_core.R.drawable.ic_signal_success
            }
        }) { Text("Loading with Icon") }

        Button(onClick = {
            Signal.loading { simpleLoading = true; dismissOnBackPress = true }
        }) { Text("Simple Loading") }
    }
}

@Composable
fun SignalTestScreen() {
    var selected by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Toast", "Snack", "Dialog", "Loading").forEachIndexed { index, label ->
                Button(
                    onClick = { selected = index },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selected == index)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) { Text(label, maxLines = 2) }
            }
        }

        when (selected) {
            0 -> ToastScreen()
            1 -> SnackScreen()
            2 -> DialogScreen()
            3 -> LoadingScreen()
        }
    }
}