package com.example.jurnal.database;

import android.content.Context;

import com.example.jurnal.data.Jurnal;
import com.example.jurnal.network.AsyncTaskRunner;
import com.example.jurnal.network.Callback;

import java.util.List;
import java.util.concurrent.Callable;

public class JurnalService {
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private JurnalDao jurnalDao;

    public JurnalService(Context context) {
        jurnalDao = DatabaseManager.getInstance(context).getJurnalDao();
    }

    public void getAll(Callback<List<Jurnal>> callback) {
        Callable<List<Jurnal>> callable = () -> jurnalDao.getAll();
        asyncTaskRunner.executeAsync(callable, callback);
    }

    public void insertAll(List<Jurnal> jurnale, Callback<List<Jurnal>> callback) {
        Callable<List<Jurnal>> callable = () -> {
            List<Long> ids = jurnalDao.insertAll(jurnale);

            for (int i = 0; i < jurnale.size(); i++) {
                jurnale.get(i).setId(ids.get(i));
            }
            return jurnale;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }

    public void delete(List<Jurnal> jurnale, Callback<List<Jurnal>> callback) {
        Callable<List<Jurnal>> callable = () -> {
            int count = jurnalDao.delete(jurnale);

            if (count <= 0) {
                return null;
            }
            return jurnale;
        };
        asyncTaskRunner.executeAsync(callable, callback);
    }
}
