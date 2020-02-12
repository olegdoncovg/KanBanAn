package com.effective.canbanan.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.effective.canbanan.R;
import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TasksDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogUtil {
    private static final String TAG = DialogUtil.class.getSimpleName();

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

    public interface CustomDialogSupplier {
        enum ButtonId {
            POSITIVE,
            NEGATIVE
        }

        void onClick(DialogInterface dialog, int id, @NonNull ButtonId button);

        @StringRes
        int getButtonTitleStringId(@NonNull ButtonId button);

        boolean isButtonAvailable(@NonNull ButtonId button);

        void onDialogCreated(AlertDialog dialog, View parentView);
    }

    public static void showCustomDialog(@NonNull Activity activity,
                                        @StringRes int titleId,
                                        @LayoutRes int layoutId,
                                        @DrawableRes int iconId,
                                        @NonNull CustomDialogSupplier supplier) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View parentView = inflater.inflate(layoutId, null);
        builder.setTitle(titleId);
        builder.setCancelable(false);
        builder.setIcon(iconId);
        builder.setView(parentView);
        for (CustomDialogSupplier.ButtonId buttonId : CustomDialogSupplier.ButtonId.values()) {
            if (supplier.isButtonAvailable(buttonId)) {
                builder.setPositiveButton(supplier.getButtonTitleStringId(buttonId),
                        (dialog, which) -> supplier.onClick(dialog, which, buttonId));
            }
        }
        AlertDialog dialog = builder.create();
        supplier.onDialogCreated(dialog, parentView);
        dialog.show();
    }

    public static void showTaskOptionDialog(@NonNull Activity activity, @NonNull Runnable updateUI,
                                            TaskItem taskItem) {
        DialogUtil.showCustomDialog(activity, R.string.approaches_to_fast_resolution,
                R.layout.custon_alert_layout, R.drawable.approaches,
                new DialogUtil.CustomDialogSupplier() {
                    @Override
                    public void onClick(DialogInterface dialog, int id, @NonNull ButtonId button) {
                        dialog.dismiss();
                    }

                    @Override
                    public int getButtonTitleStringId(@NonNull ButtonId button) {
                        return R.string.cancel;
                    }

                    @Override
                    public boolean isButtonAvailable(@NonNull ButtonId button) {
                        return button == ButtonId.NEGATIVE;
                    }

                    @Override
                    public void onDialogCreated(AlertDialog dialog, View parentView) {
                        ListView listView = parentView.findViewById(R.id.list_items);
                        if (listView == null) {
                            Log.e(TAG, "onDialogCreated: listView=null");
                            return;
                        }
                        List<String> data = new ArrayList<>(Arrays.asList(
                                activity.getString(R.string.approach_justification),
                                activity.getString(R.string.approach_special_book_reading),
                                activity.getString(R.string.approach_tony_steps),
                                activity.getString(R.string.approach_walking))
                        );
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                                activity, R.layout.recent_name_item, data);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            TasksDataModel.instance.collectActiveTime(activity, taskItem);
                            updateUI.run();
                            dialog.dismiss();
                        });
                        listView.invalidate();
                    }
                });
    }
}
