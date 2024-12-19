package com.example.jurnal.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "jurnale")
public class Jurnal {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int expense;
    private String destionation;
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public String getDestionation() {
        return destionation;
    }

    public void setDestionation(String destionation) {
        this.destionation = destionation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Jurnal(long id, int expense, String destionation, Date date) {
        this.id = id;
        this.expense = expense;
        this.destionation = destionation;
        this.date = date;
    }

    @Ignore
    public Jurnal(int expense, String destionation, Date date) {
        this.expense = expense;
        this.destionation = destionation;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jurnal jurnal = (Jurnal) o;
        return expense == jurnal.expense && Objects.equals(destionation, jurnal.destionation) && Objects.equals(date, jurnal.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expense, destionation, date);
    }

    @Override
    public String toString() {
        return "Jurnal{" +
                "id=" + id +
                ", expense=" + expense +
                ", destionation='" + destionation + '\'' +
                ", date=" + date +
                '}';
    }
}
