package com.example.exbd;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SnackDao {
    @Insert
    void inserir(Snack snack);

    @Update
    void atualizar(Snack snack);

    @Delete
    void deletar(Snack snack);

    @Query("SELECT * FROM Snack")
    LiveData<List<Snack>> listar();

    @Query("SELECT * FROM Snack WHERE id = :id LIMIT 1")
    Snack SelectId(int id);

}
