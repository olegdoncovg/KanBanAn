//package com.effective.canbanan.viewmodel;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.view.KeyEvent;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//
//import java.util.function.Consumer;
//
//public class EnterTextAlertDialog {
//
//    private final AlertDialog dialog;
//
//    public EnterTextAlertDialog(Activity activity, @NonNull Consumer<String> onResult) {
//        final EditText textEdit = new EditText(activity);
//
//        //Keyboard reaction for fast enter
//        textEdit.setOnEditorActionListener((v, actionId, event) -> {
//            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
//                    || actionId == EditorInfo.IME_ACTION_DONE) {
//                onResult.accept(textEdit.getText().toString());
//                return true;
//            }
//            return false;
//        });
//
//        // Builder
//        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(activity);
//        alert.setTitle("Enter text");
//        alert.setView(textEdit);
//
//        alert.setPositiveButton("Ok", (dialog, which) -> {
//            onResult.accept(textEdit.getText().toString());
//            finish();
//        });
//
//        alert.setNegativeButton("Cancel", (dialog, which) -> {
//            onResult.accept("");
//            finish();
//        });
//
//        // Dialog
//        dialog = alert.create();
//        dialog.setOnShowListener(dialog -> {
//            InputMethodManager imm =
//                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm != null) {
//                imm.showSoftInput(textEdit, InputMethodManager.SHOW_FORCED);
////                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            }
//        });
//    }
//
//    private void finish() {
//        dialog.dismiss();
//    }
//
//    public void show() {
//        dialog.show();
//    }
//}
