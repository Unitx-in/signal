package com.unitx.signal.compose

import android.text.InputType
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
import com.unitx.signal_core.contract.type.DialogSelectionMode
import com.unitx.signal_core.contract.type.DialogType
import com.unitx.signal_core.helper.findActivity
import com.unitx.signal_core.main.Signal

@Composable
fun DialogSelection() {
    val context = LocalContext.current
    val activity = requireNotNull(context.findActivity()) { "DialogSelectionTest must be hosted in an Activity" }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Companion.CenterVertically),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Login"
                message = "Enter your credentials."
                type = DialogType.Default
                cancelable = true
                input { hint = "Username"; onInput = { Log.i("Dialog", "User: $it") } }
                input {
                    hint = "Password"
                    password = true
                    validator = { it.length >= 6 }
                    validationError = "Min 6 characters"
                    onInput = { Log.i("Dialog", "Pass set") }
                }
                positive("Login") {}
                negative("Cancel")
            }
        }) { Text("Multi Input — Login") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Set Range"
                message = "Enter min and max values."
                type = DialogType.Action
                input {
                    hint = "Min"
                    inputType = InputType.TYPE_CLASS_NUMBER
                    maxLength = 5
                    onInput = { Log.i("Dialog", "Min: $it") }
                }
                input {
                    hint = "Max"
                    inputType = InputType.TYPE_CLASS_NUMBER
                    maxLength = 5
                    onInput = { Log.i("Dialog", "Max: $it") }
                }
                positive("Apply") {}
                negative("Cancel")
            }
        }) { Text("Multi Input — Range") }

        // Radio / Single selection
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Sort By"
                type = DialogType.Default
                selection {
                    mode = DialogSelectionMode.SINGLE
                    options("Name", "Date Modified", "Size", "Type")
                    preSelected = setOf("Name")
                    onSelected = { Log.i("Dialog", "Sort: ${it.first()}") }
                }
                positive("Apply") {}
                negative("Cancel")
            }
        }) { Text("Selection — Radio (Single)") }

        // Multi-select checkboxes
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Notify Me About"
                message =
                    "I am sleepy. I need to sleep for some hours but I can't sleep in the middle of a work day."
                type = DialogType.Default
                selection {
                    mode = DialogSelectionMode.MULTI
                    options("App Updates", "Offers", "News", "Security Alerts")
                    preSelected = setOf("App Updates", "Security Alerts")
                    onSelected = { Log.i("Dialog", "Selected: ${it.joinToString()}") }
                }
                positive("Save") {}
                negative("Cancel")
            }
        }) { Text("Selection — Checkboxes (Multi)") }

        // Chip selection
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Filter By Tags"
                type = DialogType.Default
                selection {
                    mode = DialogSelectionMode.CHIP
                    options("Android", "iOS", "Web", "Backend", "Design")
                    preSelected = setOf("Android")
                    onSelected = { Log.i("Dialog", "Tags: ${it.joinToString()}") }
                }
                positive("Filter") {}
                negative("Clear")
            }
        }) { Text("Selection — Chips") }

        // Mixed: input + no selection (edge case)
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Create Label"
                message = "Name your label and pick a category."
                type = DialogType.Positive
                input {
                    hint = "Label name"
                    maxLength = 30
                    showCounter = true
                    validator = { it.isNotBlank() }
                    validationError = "Name required"
                    onInput = { Log.i("Dialog", "Label: $it") }
                }
                selection {
                    mode = DialogSelectionMode.CHIP
                    options("Work", "Personal", "Urgent", "Later")
                    onSelected = { Log.i("Dialog", "Category: ${it.joinToString()}") }
                }
                positive("Create") {}
                negative("Cancel")
            }
        }) { Text("Mixed — Input + Chips") }
    }
}