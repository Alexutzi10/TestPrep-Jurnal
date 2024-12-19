package com.example.jurnal.network;

public interface Callback<R> {
    void runResultOnUIThread(R result);
}
