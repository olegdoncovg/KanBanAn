package com.effective.canbanan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.effective.canbanan.datamodel.TaskItem;
import com.effective.canbanan.datamodel.TaskStatus;
import com.effective.canbanan.datamodel.TasksDataModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateData();
    }

    private void updateData() {
        TasksDataModel.instance.enumerate();

        //TODO fill layout
        ListView listTodo = findViewById(R.id.todo_task_list);
        addItemsAsStrings(listTodo, TasksDataModel.instance.getTasks(TaskStatus.TO_DO));

        ListView listProgress = findViewById(R.id.progress_task_list);
        addItemsAsStrings(listProgress, TasksDataModel.instance.getTasks(TaskStatus.IN_PROGRESS));

        ListView listDone = findViewById(R.id.done_task_list);
        addItemsAsStrings(listDone, TasksDataModel.instance.getTasks(TaskStatus.DONE));

    }

    private void addItemsAsStrings(ListView todo, List<TaskItem> tasks) {
        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<String>();
        for(TaskItem item:tasks) {
            your_array_list.add(item.name);
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        todo.setAdapter(arrayAdapter);
    }
}
