package com.example.exbd;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.exbd.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SnackAdapter.OnItemClickListener {

    private AppDatabase db;
    private SnackAdapter snackAdapter;
    private ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar a base de dados
        db = AppDatabase.getDatabase(getApplicationContext());

        // Inicializar o ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Configurar o RecyclerView e o Adapter
        binding.recyclerViewSnacks.setLayoutManager(new LinearLayoutManager(this));
        snackAdapter = new SnackAdapter(this);
        snackAdapter.setOnItemClickListener(this); // Configurar o listener
        binding.recyclerViewSnacks.setAdapter(snackAdapter);

        // Observar as mudanças na lista de snacks
        db.snackDao().listar().observe(this, new Observer<List<Snack>>() {
            @Override
            public void onChanged(List<Snack> snacks) {
                snackAdapter.setSnack(snacks);
            }
        });


        // Configurar o FloatingActionButton
        binding.fabAdicionar.setOnClickListener(v -> {
            // Navegar para a atividade de adicionar snack
            Intent intent = new Intent(MainActivity.this, AddSnackActivity.class);
            startActivity(intent);
        });

        binding.fabProfile.setOnClickListener(v -> {
            // Navegar para a atividade de adicionar snack
            Intent intent = new Intent(MainActivity.this, ProfileDevActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemClick(Snack snack) {
        // Navegar para a atividade de edição de snack
        Intent intent = new Intent(MainActivity.this, EditSnackActivity.class);
        intent.putExtra("snack_id", snack.getId());
        startActivity(intent);
    }

    public void deletarSnack(Snack snack) {
        executorService.execute(() -> {
            db.snackDao().deletar(snack);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}