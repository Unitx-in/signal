package com.unitx.signal.interop;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.unitx.signal.R;
import com.unitx.signal_core.contract.type.DialogSelectionMode;
import com.unitx.signal_core.contract.type.DialogType;
import com.unitx.signal_core.contract.type.LoadingType;
import com.unitx.signal_core.contract.type.SnackType;
import com.unitx.signal_core.contract.type.ToastType;
import com.unitx.signal_core.main.Signal;

import java.util.HashSet;
import java.util.Set;

public class InteropTesting extends AppCompatActivity {

    private static final String TAG = "InteropTesting";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interop_testing);

        // ---------- Toast ----------

        findViewById(R.id.btnToastSimple).setOnClickListener(v ->
                Signal.toast(this, "Simple toast"));

        findViewById(R.id.btnToastCustom).setOnClickListener(v ->
                Signal.toast(this, "Custom toast", config -> {
                    config.setType(ToastType.Success);
                    config.setDuration(3000L);
                    config.onShown(() -> Log.d(TAG, "Toast shown"));
                    config.onDismissed(() -> Log.d(TAG, "Toast dismissed"));
                }));

        // ---------- Snack ----------

        findViewById(R.id.btnSnackSimple).setOnClickListener(v ->
                Signal.snack(this, "Simple snack"));

        findViewById(R.id.btnSnackAction).setOnClickListener(v ->
                Signal.snack(this, "Changes saved", config -> {
                    config.setType(SnackType.Success);
                    config.action("Undo", () -> {
                        Log.d(TAG, "Undo tapped");
                    });
                    config.onDismissed(() -> Log.d(TAG, "Snack dismissed"));
                }));

        // ---------- Dialog: positive / negative / neutral ----------

        findViewById(R.id.btnDialogBasic).setOnClickListener(v ->
                Signal.dialog(this, config -> {
                    config.setTitle("Delete file?");
                    config.setMessage("This action cannot be undone.");
                    config.setType(DialogType.Action);

                    config.positive("Delete", scope -> {
                        Log.d(TAG, "Delete confirmed");
                        // Signal auto-dismisses — no scope.dismiss() needed here
                    });

                    config.negative("Cancel", 2, scope -> {
                        Log.d(TAG, "Cancelled");
                    });

                    config.neutral("Learn more", scope -> {
                        Log.d(TAG, "Neutral tapped");
                    });

                    config.onShown(() -> Log.d(TAG, "Dialog shown"));
                    config.onDismissed(() -> Log.d(TAG, "Dialog dismissed"));
                }));

        // ---------- Dialog: input ----------

        findViewById(R.id.btnDialogInput).setOnClickListener(v ->
                Signal.dialog(this, config -> {
                    config.setTitle("Rename file");

                    config.input(inputConfig -> {
                        inputConfig.setHint("File name");
                        inputConfig.setPrefill("Untitled");
                        inputConfig.setMaxLength(50);
                        inputConfig.setShowCounter(true);
                        // validator returns Boolean, not Unit — plain lambda works directly
                        inputConfig.setValidator(text -> !text.isEmpty());
                        inputConfig.setValidationError("Name cannot be empty");
                        inputConfig.onInput(value -> {
                            Log.d(TAG, "Input value: " + value);
                        });
                    });

                    config.positive("Rename", scope -> {
                        Log.d(TAG, "Rename confirmed");
                    });
                    config.negative("Cancel", 2, scope -> {});
                }));

        // ---------- Dialog: selection ----------

        findViewById(R.id.btnDialogSelection).setOnClickListener(v ->
                Signal.dialog(this, config -> {
                    config.setTitle("Sort by");

                    config.selection(selectionConfig -> {
                        selectionConfig.setMode(DialogSelectionMode.SINGLE);
                        selectionConfig.options("Name", "Date", "Size");

                        Set<String> preSelected = new HashSet<>();
                        preSelected.add("Name");
                        selectionConfig.setPreSelected(preSelected);

                        selectionConfig.onSelected(selected -> {
                            Log.d(TAG, "Selected: " + selected);
                        });
                    });

                    config.positive("Apply", scope -> {});
                }));

        // ---------- Loading ----------

        findViewById(R.id.btnLoading).setOnClickListener(v -> {
            Signal.loading(this, config -> {
                config.setTitle("Uploading...");
                config.setType(LoadingType.Determinate);
                config.setCancelable(true);

                config.onShown(() -> Log.d(TAG, "Loading shown"));
                config.onDismissed(() -> Log.d(TAG, "Loading dismissed"));
                config.onCancelled(() -> Log.d(TAG, "Loading cancelled by user"));
            });

            // Simulate progress updates
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Signal.updateProgress(50, "Uploading files");
            }, 800);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Signal.updateProgress(100, "Done");
                Signal.dismissLoading();
            }, 1600);
        });

        // ---------- Notification ----------

        findViewById(R.id.btnNotif).setOnClickListener(v ->
                Signal.notif(this, config -> {
                    config.setMessage("Saved to");
                    config.setHighlight("Men fashion casual outfits");
                    config.onShown(() -> Log.d(TAG, "Notif shown"));
                    config.onDismissed(() -> Log.d(TAG, "Notif dismissed"));
                }));
    }
}