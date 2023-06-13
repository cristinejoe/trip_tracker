package com.example.mytriptracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mytriptracker.entities.Vacation;

import java.util.Date;
import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert (Vacation vacation);
    @Update
    void update(Vacation vacation);
    @Delete
    void delete(Vacation vacation);
    @Query("SELECT * FROM vacations ORDER BY vacationId ASC")
    List<Vacation> getAllVacations();
    @Query("SELECT startDate FROM vacations WHERE vacationID = :vacationID")
    Date getVacationStartDate(int vacationID);
    @Query("SELECT endDate FROM vacations WHERE vacationID = :vacationID")
    Date getVacationEndDate(int vacationID);
}
