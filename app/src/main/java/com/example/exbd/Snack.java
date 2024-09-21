package com.example.exbd;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Snack {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String descricao;
    private double valor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Snack(String name, String descricao, double valor) {
        this.name = name;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
