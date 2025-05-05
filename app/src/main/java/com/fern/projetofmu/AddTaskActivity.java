package com.fern.projetofmu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText campoTextoTarefa;
    private Button botaoSalvarTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_tarefa);

        // Referência aos componentes da interface
        campoTextoTarefa = findViewById(R.id.novaTarefaTexto);
        botaoSalvarTarefa = findViewById(R.id.novaTarefaBotao);

        // Ação ao clicar no botão de salvar
        botaoSalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoTarefa = campoTextoTarefa.getText().toString().trim();

                if (!textoTarefa.isEmpty()) {
                    // Enviar a tarefa para a MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("nova_tarefa", textoTarefa);
                    setResult(RESULT_OK, resultIntent); // Define o resultado da atividade
                    finish(); // Fecha a tela e volta para a MainActivity
                } else {
                    Toast.makeText(AddTaskActivity.this, "Digite uma tarefa!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
