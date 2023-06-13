package com.example.mytriptracker.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName="excursions")

public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionId;
    private String title;
    private Date date;
    private int vacationID;

    public Excursion(int excursionId, String title, Date date, int vacationID) {
        this.excursionId = excursionId;
        this.title = title;
        this.date = date;
        this.vacationID = vacationID;
    }

    public Excursion() {
    }

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
}
