package com.example.exbd;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbd.databinding.ActivityEditSnackBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditSnackActivity extends AppCompatActivity {

    private AppDatabase db;
    private ExecutorService executorService;
    private Snack snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityEditSnackBinding binding = ActivityEditSnackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obter a instância Singleton do banco de dados
        db = AppDatabase.getDatabase(getApplicationContext());

        executorService = Executors.newSingleThreadExecutor();

        // Obter o ID do snack a partir da Intent
        int snackId = getIntent().getIntExtra("snack_id", -1);

        if (snackId != -1) {
            // Carregar o snack do banco de dados
            executorService.execute(() -> {
                snack = db.snackDao().SelectId(snackId);
                runOnUiThread(() -> {
                    if (snack != null) {
                        // Preencher os campos com os dados do snack
                        binding.editName.setText(snack.getName());
                        binding.editDescricao.setText(snack.getDescricao());
                        binding.editValor.setText(String.valueOf(snack.getValor()));
                    }
                });
            });
        }

        binding.btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = binding.editName.getText().toString();
                final String descricao = binding.editDescricao.getText().toString();
                final double valor;

                try {
                    valor = Double.parseDouble(binding.editValor.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(EditSnackActivity.this, "Valor inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Atualizar o snack
                executorService.execute(() -> {
                    snack.setName(name);
                    snack.setDescricao(descricao);
                    snack.setValor(valor);
                    db.snackDao().atualizar(snack);

                    // Voltar à thread principal para atualizar a UI
                    runOnUiThread(() -> {
                        Toast.makeText(EditSnackActivity.this, "Snack atualizado!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}