package com.example.mytriptracker.UI;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcursionDetails extends AppCompatActivity {

    String title;
    Date date;
    int excursionID;
    int vacationID;
    Excursion currentExcursion;

    EditText excursionTitle;
    EditText excursionDate;

    Excursion excursion;
    Repository repository;
    DatePickerDialog.OnDateSetListener dateListener;
    final Calendar myCalendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repository=new Repository(getApplication());
        title = getIntent().getStringExtra("title");
        excursionTitle = findViewById(R.id.excursionTitle);
        excursionTitle.setText(title);
        excursionID = getIntent().getIntExtra("excursionId", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        excursionDate = findViewById(R.id.excursionDate);

        if (getIntent().getStringExtra("date") != null) {
            try {
                date = new SimpleDateFormat("MM/dd/yyyy").parse(getIntent().getStringExtra("date"));
                excursionDate.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (date != null) {
            excursionDate.setText(dateFormat.format(date));
        }



        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                excursionDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        excursionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dateString = excursionDate.getText().toString();
                Date currentDate = null;
                try {
                    currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(dateString);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(ExcursionDetails.this, dateListener, year, month, day);
                datePickerDialog.show();
            }
        });

        Button button=findViewById(R.id.saveExcursion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String startDateString = excursionDate.getText().toString();
                Date date = null;
                try {
                    date = dateFormat.parse(startDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ExecutorService executor = Executors.newSingleThreadExecutor();
                final Date excursionDate = date;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {


                        Date vacationStartDate = repository.getVacationStartDate(vacationID);
                        Date vacationEndDate = repository.getVacationEndDate(vacationID);
                        if (excursionDate != null && vacationStartDate != null && vacationEndDate != null) {
                            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ExcursionDetails.this, "Excursion date must be within the vacation period", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            } else {
                                if (excursionID == -1) {
                                    excursion = new Excursion(0, excursionTitle.getText().toString(), excursionDate, vacationID);
                                    repository.insert(excursion);
                                } else {
                                    excursion = new Excursion(excursionID, excursionTitle.getText().toString(), excursionDate, vacationID);
                                    repository.update(excursion);
                                }

                                ExcursionDetails.this.finish();
                            }
                        }
                    }
                });

                executor.shutdown();

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.deleteExcursion:
                for(Excursion excursion : repository.getAllExcursions()){
                    if(excursion.getExcursionId() == excursionID){
                        currentExcursion = excursion;
                    }
                }
                repository.delete(currentExcursion);
                ExcursionDetails.this.finish();
                return true;
            case R.id.notify:
                String dateFromScreen = excursionDate.getText().toString();
                String myFormat = "MM/dd/YYYY";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date myDate=null;
                try {
                    myDate=sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger = myDate.getTime();
                Intent intent= new Intent(ExcursionDetails.this, MyReceiverExcursion.class);
                intent.putExtra("key","Excursion: " + title);
                PendingIntent sender=PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert,intent,PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}