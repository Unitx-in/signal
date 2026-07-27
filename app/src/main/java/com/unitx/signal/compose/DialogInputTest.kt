package com.unitx.signal.compose

import android.text.InputType
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun DialogInputTest() {
    val context = LocalContext.current
    val activity = requireNotNull(context.findActivity()) { "DialogSelectionTest must be hosted in an Activity" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Existing: multi input ---
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

        // --- Existing: single selection modes ---
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

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Notify Me About"
                message = "Choose what you'd like to be notified about."
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

        // --- New: dropdown alone ---
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Choose Country"
                type = DialogType.Default
                dropdown {
                    placeholder = "Select a country"
                    options("India", "USA", "UK", "Germany", "Japan")
                    preSelected = "India"
                    onSelected = { Log.i("Dialog", "Country: $it") }
                }
                positive("Confirm") {}
                negative("Cancel")
            }
        }) { Text("Dropdown — Country") }

        // --- New: same field type called twice — proves append, not overwrite ---
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Location"
                message = "Pick your country and state."
                type = DialogType.Default
                dropdown {
                    placeholder = "Select country"
                    options("India", "USA", "UK")
                    onSelected = { Log.i("Dialog", "Country: $it") }
                }
                dropdown {
                    placeholder = "Select state"
                    options("Delhi", "UP", "Maharashtra")
                    onSelected = { Log.i("Dialog", "State: $it") }
                }
                positive("Confirm") {}
                negative("Cancel")
            }
        }) { Text("Dropdown x2 — Country + State") }

        // --- New: all three selection modes together, same dialog ---
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Advanced Filters"
                message = "Combine radio, checkbox, and chip in one dialog."
                type = DialogType.Default
                selection {
                    label = "Single mode "
                    mode = DialogSelectionMode.SINGLE
                    options("Newest", "Oldest", "A-Z")
                    preSelected = setOf("Newest")
                    onSelected = { Log.i("Dialog", "Sort: ${it.first()}") }
                }
                selection {
                    label = "Multi mode"
                    mode = DialogSelectionMode.MULTI
                    options("Active", "Archived", "Draft")
                    onSelected = { Log.i("Dialog", "Status: ${it.joinToString()}") }
                }
                selection {
                    mode = DialogSelectionMode.CHIP
                    options("Urgent", "Later", "Someday")
                    onSelected = { Log.i("Dialog", "Tags: ${it.joinToString()}") }
                }
                positive("Apply") {}
                negative("Reset")
            }
        }) { Text("Selection — Radio + Checkbox + Chip together") }

        // --- New: everything interleaved in a deliberately non-grouped order ---
        Button(onClick = {
            Signal.dialog(activity) {
                title = "New Task"
                message = "Fill in all the details for this task."
                type = DialogType.Action
                cancelable = true
                input {
                    hint = "Task title"
                    validator = { it.isNotBlank() }
                    validationError = "Title required"
                    onInput = { Log.i("Dialog", "Title: $it") }
                }
                dropdown {
                    placeholder = "Select priority"
                    options("Low", "Medium", "High")
                    preSelected = "Medium"
                    onSelected = { Log.i("Dialog", "Priority: $it") }
                }
                selection {
                    mode = DialogSelectionMode.CHIP
                    options("Bug", "Feature", "Chore")
                    onSelected = { Log.i("Dialog", "Type: ${it.joinToString()}") }
                }
                input {
                    hint = "Notes"
                    multiLine = true
                    onInput = { Log.i("Dialog", "Notes: $it") }
                }
                selection {
                    mode = DialogSelectionMode.MULTI
                    options("Notify assignee", "Notify watchers")
                    onSelected = { Log.i("Dialog", "Notify: ${it.joinToString()}") }
                }
                dropdown {
                    placeholder = "Assign to"
                    options("Navneet", "Pooja", "Anuj")
                    onSelected = { Log.i("Dialog", "Assignee: $it") }
                }
                positive("Create Task") {}
                negative("Discard")
            }
        }) { Text("Full Mix — Input/Dropdown/Chip/Input/Checkbox/Dropdown") }

        // --- Existing: input + chip (kept as a simpler mixed case) ---
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