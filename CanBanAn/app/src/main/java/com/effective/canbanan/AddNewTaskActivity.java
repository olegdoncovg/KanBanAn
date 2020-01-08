package com.effective.canbanan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.effective.canbanan.util.DialogUtil;

public class AddNewTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_NAME = "task_name";
    public static final String EXTRA_TASK_STATUS = "task_status";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        EditText taskName = findViewById(R.id.enter_task_name);

        findViewById(R.id.ok).setOnClickListener((view) -> {
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
        });
    }
}
