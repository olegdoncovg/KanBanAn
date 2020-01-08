package com.effective.canbanan.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.effective.canbanan.R;

public class DialogUtil {
    public static void showYesNoDialog(Activity activity, @StringRes int messId, Runnable ok, Runnable cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(messId))
//                .setMessage("Покормите кота!")
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        (dialog, id) -> {
                            if (ok != null) {
                                ok.run();
                            }
                            dialog.cancel();
                        })
                .setNegativeButton(R.string.cancel,
                        (dialog, id) -> {
                            if (cancel != null) {
                                cancel.run();
                            }
                            dialog.cancel();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showMessage(Context context, @StringRes int messId) {
        Toast.makeText(context, messId, Toast.LENGTH_SHORT).show();
    }
}
