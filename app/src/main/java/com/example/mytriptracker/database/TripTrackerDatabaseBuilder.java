package com.example.mytriptracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mytriptracker.dao.ExcursionDAO;
import com.example.mytriptracker.dao.VacationDAO;
import com.example.mytriptracker.entities.Excursion;
import com.example.mytriptracker.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TripTrackerDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static volatile TripTrackerDatabaseBuilder INSTANCE;

    static TripTrackerDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TripTrackerDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TripTrackerDatabaseBuilder.class,
                                    "MyTripTrackerDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
