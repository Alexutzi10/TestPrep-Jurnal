package com.example.jurnal.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.jurnal.data.Jurnal;

import java.util.List;

@Dao
public interface JurnalDao {
    @Query("SELECT * FROM jurnale")
    List<Jurnal> getAll();

    @Insert
    List<Long> insertAll(List<Jurnal> jurnale);

    @Delete
    int delete(List<Jurnal> jurnale);
}
