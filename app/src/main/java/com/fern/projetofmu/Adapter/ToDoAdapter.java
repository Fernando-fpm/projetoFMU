package com.fern.projetofmu.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fern.projetofmu.MainActivity;
import com.fern.projetofmu.R;
import com.fern.projetofmu.Utils.AddNovaTarefa;
import com.fern.projetofmu.Utils.GerenciadorBD;
import com.fern.projetofmu.model.ToDoModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private GerenciadorBD db;
    private Context context;

    // Construtor agora recebe o context para evitar null pointer
    public ToDoAdapter(GerenciadorBD db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public Context getContext() {
        return context;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Garantindo que o context seja inicializado mesmo se constructor não for usado (segurança)
        if (context == null) {
            context = parent.getContext();
        }
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.tarefa_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Abrindo o banco aqui pode ser pesado, então garanta que não fica abrindo toda hora desnecessariamente
        db.openDatabase();

        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));

        // Remove listener anterior para evitar chamadas erradas ao reciclar views
        holder.task.setOnCheckedChangeListener(null);
        holder.task.setChecked(toBoolean(item.getStatus()));

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.openDatabase(); // garante que está aberto
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (todoList == null) ? 0 : todoList.size();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public void deleteItem(int position) {
        if (todoList != null && position >= 0 && position < todoList.size()) {
            db.openDatabase();
            ToDoModel item = todoList.get(position);
            db.deleteTask(item.getId());

            todoList.remove(position);
            notifyItemRemoved(position);

            // Opcional: notificar que mudou o dataset inteiro para evitar inconsistências
            notifyItemRangeChanged(position, todoList.size());
        }
    }

    public void editItem(int position) {
        if (todoList != null && position >= 0 && position < todoList.size()) {
            ToDoModel item = todoList.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("tarefa", item.getTask());

            AddNovaTarefa fragment = new AddNovaTarefa();
            fragment.setArguments(bundle);
            fragment.show(activity.getSupportFragmentManager(), AddNovaTarefa.TAG);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.tarefacheckbox);
        }
    }
}
