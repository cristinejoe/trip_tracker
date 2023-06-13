package com.example.mytriptracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytriptracker.R;
import com.example.mytriptracker.database.Repository;
import com.example.mytriptracker.entities.Excursion;
import com.example.mytriptracker.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationDetails extends AppCompatActivity {

    EditText editTittle;
    EditText editHotel;
    EditText editStartDate;
    EditText editEndDate;

    String vacationTitle;
    String hotel;
    Date startDate;
    Date endDate;
    int vacationID;
    Vacation vacation;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDateListener;
    DatePickerDialog.OnDateSetListener endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        editTittle = findViewById(R.id.vacationTitle);
        editHotel = findViewById(R.id.hotel);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        vacationTitle = getIntent().getStringExtra("vacationTitle");
        hotel = getIntent().getStringExtra("hotel");
        vacationID = getIntent().getIntExtra("vacationID", -1);

        if (getIntent().getStringExtra("startDate") != null) {
            try {
                startDate = new SimpleDateFormat("MM/dd/yyyy").parse(getIntent().getStringExtra("startDate"));
                editStartDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (getIntent().getStringExtra("endDate") != null) {
            try {
                endDate = new SimpleDateFormat("MM/dd/yyyy").parse(getIntent().getStringExtra("endDate"));
                editEndDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editTittle.setText(vacationTitle);
        editHotel.setText(hotel);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (startDate != null) {
            editStartDate.setText(dateFormat.format(startDate));
        }
        if (endDate != null) {
            editEndDate.setText(dateFormat.format(endDate));
        }

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editStartDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDateString = editStartDate.getText().toString();
                Date currentDate = null;
                try {
                    currentDate = new SimpleDateFormat("MM/dd/yyyy").parse(startDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                if (currentDate != null) {
                    calendar.setTime(currentDate);
                }
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(VacationDetails.this, startDateListener, year, month, day);
                    datePickerDialog.show();

            }
        });

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editEndDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endDateString = editEndDate.getText().toString();
                Date currentDate = null;
                try {
                    currentDate = new SimpleDateFormat("MM/dd/yyyy").parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Calendar calendar = Calendar.getInstance();
                if (currentDate != null) {
                    calendar.setTime(currentDate);
                }
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(VacationDetails.this, endDateListener, year, month, day);
                    datePickerDialog.show();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //excursionAdapter.setExcursions(repository.getAllExcursions());
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        Button button=findViewById(R.id.saveVacation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String startDateString = editStartDate.getText().toString();
                String endDateString = editEndDate.getText().toString();
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = dateFormat.parse(startDateString);
                    endDate = dateFormat.parse(endDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (editTittle.getText().toString().trim().length() == 0 || startDate == null || endDate == null) {
                    Toast.makeText(VacationDetails.this, "Please check for empty fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (endDate.before(startDate)) {
                    Toast.makeText(VacationDetails.this, "End date cannot be before start date", Toast.LENGTH_LONG).show();
                    return;
                }

                if(vacationID == -1){
                    vacation = new Vacation(0, editTittle.getText().toString(), editHotel.getText().toString(), startDate, endDate);
                    repository.insert(vacation);
                    Intent intent = new Intent(VacationDetails.this, VacationsList.class);
                    startActivity(intent);
                    finish();

                }else{
                    vacation = new Vacation(vacationID, editTittle.getText().toString(), editHotel.getText().toString(), startDate, endDate);
                    repository.update(vacation);
                    Intent intent = new Intent(VacationDetails.this, VacationsList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteVacation:
                for (Vacation vacation : repository.getAllVacations()) {
                    if (vacation.getVacationId() == vacationID) currentVacation = vacation;
                }

                numExcursions = 0;
                for (Excursion excursion : repository.getAllExcursions()) {
                    if (excursion.getVacationID() == vacationID) ++numExcursions;
                }

                if (numExcursions == 0) {
                    repository.delete(currentVacation);
                    Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
                    VacationDetails.this.finish();
                } else {
                    Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.addExcursion:
                if(vacationID == -1){
                    Toast.makeText(VacationDetails.this, "Please save the vacation first before adding an excursion", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(VacationDetails.this,ExcursionDetails.class);
                    intent.putExtra("vacationID", vacationID);
                    startActivity(intent);
                }
                return true;

            case R.id.notifyStart:
                String startDateFromScreen = editStartDate.getText().toString();
                String myStartFormat = "MM/dd/YYYY";
                SimpleDateFormat sdfStart = new SimpleDateFormat(myStartFormat, Locale.US);
                Date myStartDate = null;
                try {
                    myStartDate = sdfStart.parse(startDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long triggerStart = myStartDate.getTime();
                Intent intentStart = new Intent(VacationDetails.this, MyReceiverVacationStart.class);
                intentStart.putExtra("key","Vacation: " + vacationTitle + " is starting");
                PendingIntent senderStart = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert,intentStart,PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManagerStart = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManagerStart.set(AlarmManager.RTC_WAKEUP, triggerStart, senderStart);
                return true;

            case R.id.notifyEnd:
                String endDateFromScreen = editEndDate.getText().toString();
                String myEndFormat = "MM/dd/YYYY";
                SimpleDateFormat sdfEnd = new SimpleDateFormat(myEndFormat, Locale.US);
                Date myEndDate = null;
                try {
                    myEndDate = sdfEnd.parse(endDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long triggerEnd = myEndDate.getTime();
                Intent intentEnd = new Intent(VacationDetails.this, MyReceiverVacationEnd.class);
                intentEnd.putExtra("key","Vacation: " + vacationTitle + " is ending");
                PendingIntent senderEnd = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert,intentEnd,PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManagerEnd = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManagerEnd.set(AlarmManager.RTC_WAKEUP, triggerEnd, senderEnd);
                return true;

            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Details");
                sendIntent.setType("text/plain");

                // Use an ExecutorService to run database queries off the main thread
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Get excursion details from the database
                        List<Excursion> excursions = repository.getAllAssociatedExcursions(vacationID);
                        StringBuilder excursionDetails = new StringBuilder("\n\nExcursions:\n");
                        if (excursions != null && excursions.size() > 0) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                            for (Excursion excursion : excursions) {
                                excursionDetails.append("\n- ").append(excursion.getTitle()).append(" (").append(dateFormat.format(excursion.getDate())).append(")");
                            }
                        }

                        String vacationTitle = getIntent().getStringExtra("vacationTitle");
                        String vacationLocation = getIntent().getStringExtra("hotel");
                        String vacationStartDate = null;
                        String vacationEndDate = null;
                        if (getIntent().getStringExtra("startDate") != null || getIntent().getStringExtra("endDate") != null) {
                            try {
                                if (getIntent().getStringExtra("startDate") != null) {
                                    Date startDate = new SimpleDateFormat("MM/dd/yyyy").parse(getIntent().getStringExtra("startDate"));
                                    vacationStartDate = new SimpleDateFormat("MM/dd/yyyy").format(startDate);
                                }
                                if (getIntent().getStringExtra("endDate") != null) {
                                    Date endDate = new SimpleDateFormat("MM/dd/yyyy").parse(getIntent().getStringExtra("endDate"));
                                    vacationEndDate = new SimpleDateFormat("MM/dd/yyyy").format(endDate);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        String message = "Vacation Details:\n" +
                                "- Title: " + vacationTitle + "\n" +
                                "- Location: " + vacationLocation + "\n" +
                                "- Start Date: " + vacationStartDate + "\n" +
                                "- End Date: " + vacationEndDate +
                                excursionDetails.toString();

                        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                    }
                });

                return true;
        }
            return super.onOptionsItemSelected(item);
    }

    protected void onResume() {

        super.onResume();
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }
}