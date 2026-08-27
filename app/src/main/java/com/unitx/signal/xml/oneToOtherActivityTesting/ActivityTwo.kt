package com.unitx.signal.xml.oneToOtherActivityTesting

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unitx.signal.R
import com.unitx.signal_core.contract.type.DialogType
import com.unitx.signal_core.main.Signal

class ActivityTwo : AppCompatActivity() {

    private var updateDialogShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_xml_testing2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d("SignalRepro", "Testing2: onCreate")

        showCommitOrderTestDialog {
            showLifecycleTestDialog()
        }
    }

    // --- Repro for the field-commit-order bug ---
    // Expected AFTER the fix: log line (1) appears before log line (2), and
    // capturedValue is non-null in (2).
    // Before the fix: (2) fires first and capturedValue is still null.
    private fun showCommitOrderTestDialog(onFinished: () -> Unit) {
        var capturedValue: String? = null
        Signal.dialog(this) {
            title = "Commit order test"
            message = "Pick an option, then tap Ok, without touching anything else first."
            cancelable = false
            type = DialogType.Action

            dropdown {
                placeholder = "Select from the list"
                options("Alpha", "Beta", "Gamma")
                validator = { it != null }
                validationError = "Choose one"
                onSelected {
                    Log.d("SignalRepro", "(1) onSelected fired, value=[$it]")
                    capturedValue = it
                }
            }

            positive("Ok") {
                Log.d("SignalRepro", "(2) positive fired, capturedValue=[$capturedValue]")
            }
            onDismissed = { onFinished() }
        }
    }
    // --- end repro ---

    private fun showLifecycleTestDialog() {
        Signal.dialog(this) {
            title = "Update Required"
            message = "Does this dialog stay open, or does it vanish when Testing1 is destroyed?"
            cancelable = false
            type = DialogType.Error
            positive("OK"){
                Signal.dialog(this@ActivityTwo){
                    title = "2222Successful purchase!"
                    message = "2222Your order has been placed and will arrive in 3-5 business days."
                    type = DialogType.Error
                    positive("ok"){

                    }
                }
            }
            negative("Cancel")
            showCloseButton = false
            onShown = { Log.d("SignalRepro", "Dialog: onShown") }
            onDismissed = { Log.d("SignalRepro", "Dialog: onDismissed") }
        }
    }

    override fun onDestroy() {
        Log.d("SignalRepro", "Testing2: onDestroy")
        super.onDestroy()
    }
}