package com.effective.canbanan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.effective.canbanan.datamodel.SortOption;
import com.effective.canbanan.datamodel.TasksDataModel;
import com.effective.canbanan.util.DialogUtil;

import java.util.List;

public class AddNewTaskActivity extends AppCompatActivity {
    private static final String TAG = AddNewTaskActivity.class.getSimpleName();
    private static final int LIST_ITEMS_MAX_COUNT = 10;

    public static final String EXTRA_TASK_NAME = "task_name";
    public static final String EXTRA_TASK_STATUS = "task_status";

    private EditText taskName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        taskName = findViewById(R.id.enter_task_name);
        taskName.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d(TAG, "onKeyboard: actionId=" + actionId + ", checkDataAndFinish!");
                checkDataAndFinish();
                return true;
            }
            return false;
        });

        findViewById(R.id.createTask).setOnClickListener((view) -> {
            checkDataAndFinish();
        });

        fillRecentNames(SortOption.POPULAR, R.id.popular_names);
        fillRecentNames(SortOption.RECENT, R.id.recent_names);
    }

    private void fillRecentNames(@NonNull SortOption sortOption, @IdRes int listViewId) {
        ListView listView = findViewById(listViewId);
        List<String> data = TasksDataModel.instance.getStatisticNames(
                this, sortOption, LIST_ITEMS_MAX_COUNT);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(
                this, R.layout.recent_name_item, data);
        listView.setAdapter(arrayAdapter2);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            taskName.setText(data.get(position));
            checkDataAndFinish();
        });
    }

    private void checkDataAndFinish() {
        String text = taskName.getText().toString();
        if (TextUtils.isEmpty(text)) {
            DialogUtil.showMessage(AddNewTaskActivity.this, R.string.empty_string);
            return;
        }

        Intent result = new Intent();
        result.putExtra(EXTRA_TASK_STATUS, getIntent().getStringExtra(EXTRA_TASK_STATUS));
        result.putExtra(EXTRA_TASK_NAME, text);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
