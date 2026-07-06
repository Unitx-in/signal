package com.unitx.signal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivityXmlTesting1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_xml_testing1)

        val root = findViewById<ViewGroup>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Programmatically added so we don't need to touch your XML layout.
        val goButton = Button(this).apply {
            text = "Go to Testing2 (and finish this activity)"
        }
        root.addView(
            goButton,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        goButton.setOnClickListener {
            Log.d("SignalRepro", "Testing1: starting Testing2")
            startActivity(Intent(this, MainActivityXmlTesting2::class.java))
            finish() // <-- mimics your real login→Home flow (finish after navigating)
        }
    }

    override fun onDestroy() {
        Log.d("SignalRepro", "Testing1: onDestroy")
        super.onDestroy()
    }
}