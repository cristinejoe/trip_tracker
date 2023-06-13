package com.example.mytriptracker.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName="vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationId;
    private String vacationTitle;
    private String hotel;
    private Date startDate;
    private Date endDate;

    public Vacation(int vacationId, String vacationTitle, String hotel, Date startDate, Date endDate) {
        this.vacationId = vacationId;
        this.vacationTitle = vacationTitle;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Vacation() {
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
