package com.fern.projetofmu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fern.projetofmu.Adapter.ToDoAdapter;
import com.fern.projetofmu.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView tarefaRecyclerView;
    private ToDoAdapter tasksAdapter;
    private List<ToDoModel> taskList;

    private static final int REQUEST_CODE_ADD_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tarefaRecyclerView = findViewById(R.id.tasksRecyclerView);
        tarefaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        tasksAdapter = new ToDoAdapter(this);
        tarefaRecyclerView.setAdapter(tasksAdapter);

        //  quando o botão add for clicado a AddTaskActivity é chamada
        findViewById(R.id.fab).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
        });
    }

    // Recebe a tarefa de volta da AddTaskActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            String novaTarefa = data.getStringExtra("nova_tarefa");
            ToDoModel task = new ToDoModel();
            task.setTask(novaTarefa);
            task.setStatus(0); // Defina o status como 0 (não concluída)
            taskList.add(task);
            tasksAdapter.setTasks(taskList); // Atualiza o RecyclerView
            Toast.makeText(this, "Tarefa adicionada: " + novaTarefa, Toast.LENGTH_SHORT).show();
        }
    }
}