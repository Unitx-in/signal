package com.unitx.signal.xml

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unitx.signal_core.contract.type.DialogSelectionMode
import com.unitx.signal_core.main.Signal

class NestingDialog : AppCompatActivity() {

    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val scroll = ScrollView(this).apply { addView(root) }
        setContentView(scroll)

        ViewCompat.setOnApplyWindowInsetsListener(scroll) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        addTestButtons()
    }

    private fun btn(label: String, action: () -> Unit) {
        root.addView(Button(this).apply {
            text = label
            setOnClickListener { action() }
        })
    }

    private fun addTestButtons() {

        // ---------- 1. RAPID DOUBLE-DISMISS (the exact race we diagnosed) ----------
        btn("1. Rapid double-tap dismiss (manual x2 fast)") {
            Signal.dialog(this) {
                title = "Double Dismiss Test"
                message = "Tapping positive should not double-fire queue.next()"
                positive("OK") {
                    // Simulate a user double-tapping / two dismiss triggers landing
                    // near-simultaneously, before the scaleOut animation completes.
                    Signal.dismissDialog()
                    Signal.dismissDialog()
                }
            }
            // Immediately queue a second dialog behind it — if the queue gets
            // corrupted, this one either never shows or shows and instantly dies.
            Signal.dialog(this) {
                title = "Dialog B"
                message = "If you see this cleanly, the queue survived."
                positive("Got it")
            }
        }

        // ---------- 2. AUTO-DISMISS RACE vs MANUAL DISMISS ----------
        btn("2. Auto-dismiss race (tap OK right as timer fires)") {
            Signal.dialog(this) {
                title = "Auto-dismiss Race"
                message = "Has a 1500ms auto-dismiss. Tap OK right around then."
                autoDismiss = true
                autoDismissDuration = 1500L
                positive("OK") { }
            }
            Signal.dialog(this) {
                title = "Dialog After Race"
                message = "Should show cleanly if scheduler + manual dismiss don't collide."
                positive("Fine")
            }
        }

        // ---------- 3. NESTED DIALOGS — chain of 4, each opened from positive() ----------
        btn("3. Nested dialogs (4 levels deep)") {
            showNestedLevel(1, maxLevel = 4)
        }

        // ---------- 4. QUEUE STRESS — fire many dialogs synchronously ----------
        btn("4. Queue stress — fire 6 dialogs synchronously") {
            for (i in 1..6) {
                Signal.dialog(this) {
                    title = "Queued Dialog #$i"
                    message = "Auto-advances after 400ms"
                    autoDismiss = true
                    autoDismissDuration = 400L
                    positive("Next")
                }
            }
        }

        // ---------- 5. NESTED + AUTO-DISMISS COMBINED (worst case) ----------
        btn("5. Nested dialogs, each auto-dismissing + manual race") {
            showNestedAutoDismissLevel(1, maxLevel = 3)
        }

        // ---------- 6. ACTIVITY DESTROYED WHILE DIALOG SHOWING ----------
        btn("6. Finish activity mid-dialog (tests onOwningActivityDestroyed race)") {
            Signal.dialog(this) {
                title = "About to be destroyed"
                message = "Activity finish() will fire right after this shows."
                cancelable = false
                positive("Won't get tapped")
            }
            root.postDelayed({ finish() }, 300)
        }

        // ---------- 7. DISMISS + IMMEDIATE NEW DIALOG FROM SAME CALLBACK ----------
        btn("7. Dismiss triggers new dialog synchronously from same thread") {
            Signal.dialog(this) {
                title = "Step 1"
                message = "Positive will immediately enqueue Step 2 before this finishes dismissing."
                positive("Next") {
                    Signal.dialog(this@NestingDialog) {
                        title = "Step 2"
                        message = "Fired synchronously inside Step 1's positive callback."
                        positive("Next") {
                            Signal.dialog(this@NestingDialog) {
                                title = "Step 3"
                                message = "Third level, same-thread chain."
                                positive("Done")
                            }
                        }
                    }
                }
            }
        }

        // ---------- 8. ALL FIELD TYPES STACKED IN ONE DIALOG ----------
        btn("8. All field types stacked (input+selection+chip+dropdown)") {
            Signal.dialog(this) {
                title = "Full Form"
                message = "Every field type in one dialog."
                input {
                    hint = "Your name"
                    validator = { it.isNotBlank() }
                    validationError = "Name required"
                }
                input {
                    hint = "Password"
                    password = true
                }
                selection {
                    label = "Choose one (radio)"
                    mode = DialogSelectionMode.SINGLE
                    options("Option A", "Option B", "Option C")
                }
                selection {
                    label = "Choose many (checkbox)"
                    mode = DialogSelectionMode.MULTI
                    options("Red", "Green", "Blue")
                }
                selection {
                    label = "Chips"
                    mode = DialogSelectionMode.CHIP
                    options("Tag1", "Tag2", "Tag3")
                }
                dropdown {
                    placeholder = "Pick a size"
                    options("Small", "Medium", "Large")
                }
                positive("Submit")
                negative("Cancel")
                neutral("Reset")
            }
        }

        // ---------- 9. BACK-PRESS CANCEL WHILE ANOTHER DIALOG QUEUED ----------
        btn("9. Back-press cancel with dialog queued behind it") {
            Signal.dialog(this) {
                title = "Cancelable"
                message = "Press back to dismiss (or tap overlay)."
                cancelable = true
                positive("OK")
            }
            Signal.dialog(this) {
                title = "Queued Behind Cancelable"
                message = "Should show once the first is back-pressed away."
                positive("OK")
            }
        }

        // ---------- 10. MIXED SIGNAL TYPES INTERLEAVED ----------
        btn("10. Interleave toast/snack/loading around dialogs") {
            Signal.toast(this, "Starting sequence")
            Signal.loading(this) { title = "Loading..." }
            root.postDelayed({
                Signal.dismissLoading()
                Signal.dialog(this) {
                    title = "Loaded"
                    message = "Loading dismissed, dialog shown."
                    positive("OK") {
                        Signal.snack(this@NestingDialog, "Confirmed via snack")
                    }
                }
            }, 1000)
        }

        // ---------- 11. PREVENT + ASYNC DISMISS (DialogScope.prevent/dismiss) ----------
        btn("11. prevent() then async dismiss() — race with queue.next()") {
            Signal.dialog(this) {
                title = "Async Submit"
                message = "Positive prevents auto-dismiss, dismisses after 800ms async work."
                positive("Submit") {
                    prevent()
                    root.postDelayed({ dismiss() }, 800)
                }
            }
            Signal.dialog(this) {
                title = "Next In Queue"
                message = "Should only show after the async dismiss above completes."
                positive("OK")
            }
        }

        // ---------- 12. RAPID-FIRE dismissDialog() calls (extreme stress) ----------
        btn("12. Call dismissDialog() 10x in a tight loop") {
            Signal.dialog(this) {
                title = "Extreme Stress"
                message = "dismissDialog() called 10x immediately."
                positive("OK")
            }
            repeat(10) { Signal.dismissDialog() }
            Signal.dialog(this) {
                title = "Survivor"
                message = "If this shows and stays visible, the guard held."
                positive("OK")
            }
        }
    }

    private fun showNestedLevel(level: Int, maxLevel: Int) {
        Signal.dialog(this) {
            title = "Nested Level $level"
            message = "Depth $level of $maxLevel"
            positive(if (level < maxLevel) "Next level" else "Finish") {
                if (level < maxLevel) {
                    showNestedLevel(level + 1, maxLevel)
                }
            }
            negative("Stop here")
        }
    }

    private fun showNestedAutoDismissLevel(level: Int, maxLevel: Int) {
        Signal.dialog(this) {
            title = "Auto Nested $level"
            message = "Auto-dismisses in 600ms, then chains to next level."
            autoDismiss = true
            autoDismissDuration = 600L
            onDismissed {
                if (level < maxLevel) {
                    showNestedAutoDismissLevel(level + 1, maxLevel)
                }
            }
            positive("Skip ahead") {
                // Manual dismiss racing the scheduled auto-dismiss —
                // exercises the exact scheduler.cancel() timing question.
            }
        }
    }
}