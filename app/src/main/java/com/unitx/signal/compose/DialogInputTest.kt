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
                type = DialogType.Warning
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

        // --- Existing: single selection modes, now with validators ---
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Sort By"
                type = DialogType.Warning
                selection {
                    mode = DialogSelectionMode.SINGLE
                    options("Name", "Date Modified", "Size", "Type")
                    preSelected = emptySet() // intentionally empty — forces validator to fail on first tap
                    validator = { it.isNotEmpty() }
                    validationError = "Pick a sort order"
                    onSelected = { Log.i("Dialog", "Sort: ${it.first()}") }
                }
                positive("Apply") {}
                negative("Cancel")
            }
        }) { Text("Selection — Radio (Single, validated)") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Notify Me About"
                message = "Choose what you'd like to be notified about."
                type = DialogType.Warning
                selection {
                    mode = DialogSelectionMode.MULTI
                    options("App Updates", "Offers", "News", "Security Alerts")
                    preSelected = emptySet()
                    validator = { it.isNotEmpty() }
                    validationError = "Select at least one option"
                    onSelected = { Log.i("Dialog", "Selected: ${it.joinToString()}") }
                }
                positive("Save") {}
                negative("Cancel")
            }
        }) { Text("Selection — Checkboxes (Multi, validated)") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Filter By Tags"
                type = DialogType.Warning
                selection {
                    mode = DialogSelectionMode.CHIP
                    options("Android", "iOS", "Web", "Backend", "Design")
                    preSelected = emptySet()
                    validator = { it.size <= 3 }
                    validationError = "Pick at most 3 tags"
                    onSelected = { Log.i("Dialog", "Tags: ${it.joinToString()}") }
                }
                positive("Filter") {}
                negative("Clear")
            }
        }) { Text("Selection — Chips (max 3, validated)") }

        // --- Existing: dropdown, now with validator ---
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Choose Country"
                type = DialogType.Warning
                dropdown {
                    placeholder = "Select a country"
                    options("India", "USA", "UK", "Germany", "Japan")
                    preSelected = null
                    validator = { it != null }
                    validationError = "Please select a country"
                    onSelected = { Log.i("Dialog", "Country: $it") }
                }
                positive("Confirm") {}
                negative("Cancel")
            }
        }) { Text("Dropdown — Country (validated)") }

        Button(onClick = {
            Signal.dialog(activity) {
                title = "Location"
                message = "Pick your country and state."
                type = DialogType.Warning
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

        // --- Existing: all three selection modes together, now labeled + validated ---
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Advanced Filters"
                message = "Combine radio, checkbox, and chip in one dialog."
                type = DialogType.Warning
                selection {
                    label = "Sort by"
                    mode = DialogSelectionMode.SINGLE
                    options("Newest", "Oldest", "A-Z")
                    preSelected = setOf("Newest")
                    onSelected = { Log.i("Dialog", "Sort: ${it.first()}") }
                }
                selection {
                    label = "Status"
                    mode = DialogSelectionMode.MULTI
                    options("Active", "Archived", "Draft")
                    preSelected = emptySet()
                    validator = { it.isNotEmpty() }
                    validationError = "Select at least one status"
                    onSelected = { Log.i("Dialog", "Status: ${it.joinToString()}") }
                }
                selection {
                    label = "Tags"
                    mode = DialogSelectionMode.CHIP
                    options("Urgent", "Later", "Someday")
                    onSelected = { Log.i("Dialog", "Tags: ${it.joinToString()}") }
                }
                positive("Apply") {}
                negative("Reset")
            }
        }) { Text("Selection — Radio + Checkbox + Chip together") }

        // --- Existing: full mix, now with validators on every field type at once ---
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
                    preSelected = null
                    validator = { it != null }
                    validationError = "Priority required"
                    onSelected = { Log.i("Dialog", "Priority: $it") }
                }
                selection {
                    label = "Type"
                    mode = DialogSelectionMode.CHIP
                    options("Bug", "Feature", "Chore")
                    preSelected = emptySet()
                    validator = { it.isNotEmpty() }
                    validationError = "Pick a type"
                    onSelected = { Log.i("Dialog", "Type: ${it.joinToString()}") }
                }
                input {
                    hint = "Notes"
                    multiLine = true
                    onInput = { Log.i("Dialog", "Notes: $it") }
                }
                selection {
                    label = "Notify"
                    mode = DialogSelectionMode.MULTI
                    options("Notify assignee", "Notify watchers")
                    onSelected = { Log.i("Dialog", "Notify: ${it.joinToString()}") }
                }
                dropdown {
                    placeholder = "Assign to"
                    options("Navneet", "Pooja", "Anuj")
                    preSelected = null
                    validator = { it != null }
                    validationError = "Assignee required"
                    onSelected = { Log.i("Dialog", "Assignee: $it") }
                }
                positive("Create Task") {}
                negative("Discard")
            }
        }) { Text("Full Mix — all fields validated at once") }

        // --- Existing: input + chip ---
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

        // --- NEW: dedicated validation-only tests ---

        // Proves: submit is blocked when the single required field is empty.
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Required Field"
                message = "Tap Submit without typing anything — it should not dismiss."
                type = DialogType.Action
                input {
                    hint = "Your name"
                    validator = { it.isNotBlank() }
                    validationError = "This field is required"
                    onInput = { Log.i("Dialog", "Name: $it") }
                }
                positive("Submit") {}
                negative("Cancel")
            }
        }) { Text("Validation — Single required input") }

        // Proves: all field errors surface together, not just the first failing one.
        Button(onClick = {
            Signal.dialog(activity) {
                title = "All Fields Required"
                message = "Tap Submit immediately — every field below should show its own error at once."
                type = DialogType.Warning
                input {
                    hint = "Email"
                    validator = { it.contains("@") }
                    validationError = "Enter a valid email"
                    onInput = { Log.i("Dialog", "Email: $it") }
                }
                dropdown {
                    placeholder = "Select role"
                    options("Admin", "Editor", "Viewer")
                    validator = { it != null }
                    validationError = "Role is required"
                    onSelected = { Log.i("Dialog", "Role: $it") }
                }
                selection {
                    label = "Permissions"
                    mode = DialogSelectionMode.MULTI
                    options("Read", "Write", "Delete")
                    validator = { it.isNotEmpty() }
                    validationError = "Select at least one permission"
                    onSelected = { Log.i("Dialog", "Permissions: ${it.joinToString()}") }
                }
                positive("Submit") {}
                negative("Cancel")
            }
        }) { Text("Validation — All 3 field types fail at once") }

        // Proves: fixing a field clears only that field's error, without needing to retry others.
        Button(onClick = {
            Signal.dialog(activity) {
                title = "Fix And Retry"
                message = "Submit once to see the error, then type a valid value and submit again."
                type = DialogType.Warning
                input {
                    hint = "4-digit PIN"
                    maxLength = 4
                    inputType = InputType.TYPE_CLASS_NUMBER
                    validator = { it.length == 4 }
                    validationError = "PIN must be exactly 4 digits"
                    onInput = { Log.i("Dialog", "PIN set") }
                }
                positive("Confirm") {}
                negative("Cancel")
            }
        }) { Text("Validation — Fix and resubmit") }
    }
}