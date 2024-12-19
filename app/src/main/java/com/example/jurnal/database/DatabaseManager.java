package com.example.jurnal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.jurnal.data.DateConverter;
import com.example.jurnal.data.Jurnal;

@Database(entities = {Jurnal.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class DatabaseManager extends RoomDatabase {
    public static DatabaseManager connection;

    public static DatabaseManager getInstance(Context context) {
        if (connection != null) {
            return connection;
        }

        connection = Room.databaseBuilder(context, DatabaseManager.class, "jurnal_db").fallbackToDestructiveMigration().build();

        return connection;
    }

    public abstract JurnalDao getJurnalDao();
}
