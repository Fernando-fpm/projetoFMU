package com.fern.projetofmu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fern.projetofmu.Adapter.ToDoAdapter;
import com.fern.projetofmu.Utils.AddNovaTarefa;
import com.fern.projetofmu.Utils.GerenciadorBD;
import com.fern.projetofmu.model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tarefaRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private GerenciadorBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tarefaRecyclerView = findViewById(R.id.tasksRecyclerView);
        tarefaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new GerenciadorBD(this);
        db.openDatabase();

        tasksAdapter = new ToDoAdapter(db, this);
        tarefaRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            AddNovaTarefa.newInstance().show(getSupportFragmentManager(), AddNovaTarefa.TAG);
        });

        DeslizeRecycler deslizeRecycler = new DeslizeRecycler(tasksAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(deslizeRecycler);
        itemTouchHelper.attachToRecyclerView(tarefaRecyclerView);

        loadTasks();
    }

    private void loadTasks() {
        List<ToDoModel> taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        loadTasks();
        tasksAdapter.notifyDataSetChanged();
    }
}
