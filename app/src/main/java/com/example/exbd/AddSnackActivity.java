package com.example.exbd;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exbd.databinding.ActivityAddSnackBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddSnackActivity extends AppCompatActivity {

    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityAddSnackBinding binding = ActivityAddSnackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        db = AppDatabase.getDatabase(getApplicationContext());


        // Inicializar o ExecutorService
        executorService = Executors.newSingleThreadExecutor();


       binding.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = binding.editName.getText().toString();
                final String descricao = binding.editDescricao.getText().toString();
                final double valor;

                try {
                    valor = Double.parseDouble(binding.editValor.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(AddSnackActivity.this, "Valor inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Executar a inserção em background
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.snackDao().inserir(new Snack(name, descricao, valor));

                        // Retornar à thread principal para atualizar a UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddSnackActivity.this, "Snack salvo!", Toast.LENGTH_SHORT).show();
                                finish(); // Volta para a MainActivity
                            }
                        });
                    }
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