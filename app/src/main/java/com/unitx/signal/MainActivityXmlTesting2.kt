package com.unitx.signal

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unitx.signal_core.contract.type.DialogType
import com.unitx.signal_core.main.Signal

class MainActivityXmlTesting2 : AppCompatActivity() {

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
        Signal.dialog(this) {
            title = "Update Required"
            message = "Does this dialog stay open, or does it vanish when Testing1 is destroyed?"
            cancelable = false
            type = DialogType.Error
            positive("OK")
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