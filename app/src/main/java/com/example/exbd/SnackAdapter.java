package com.example.exbd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SnackAdapter extends RecyclerView.Adapter<SnackAdapter.SnackViewHolder> {
    private List<Snack> snacks = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(Snack snack);
    }

    public SnackAdapter(Context context) {
        this.context = context;
    }

    public void setSnack(List<Snack> snacks) {
        this.snacks.clear();
        this.snacks.addAll(snacks);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SnackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_snack, parent, false);
        return new SnackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SnackViewHolder holder, int position) {
        Snack snack = snacks.get(position);
        holder.textName.setText("Name: " + snack.getName());
        holder.textDescricao.setText("Descrição: " + snack.getDescricao());
        holder.textValor.setText(String.format("Valor: R$ %.2f", snack.getValor()));

        // Clique no item para editar
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(snack);
            }
        });

        // Clique no botão de deletar
        holder.btnDelete.setOnClickListener(v -> {
            // Exibir um diálogo de confirmação
            new AlertDialog.Builder(context)
                    .setTitle("Excluir Snack")
                    .setMessage("Tem certeza que deseja excluir este snack?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).deletarSnack(snack);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return snacks != null ? snacks.size() : 0;
    }

    public static class SnackViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDescricao, textValor;
        ImageButton btnDelete;

        public SnackViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textValor = itemView.findViewById(R.id.textValor);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
