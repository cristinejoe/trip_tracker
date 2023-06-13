package com.example.mytriptracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytriptracker.R;
import com.example.mytriptracker.database.Repository;
import com.example.mytriptracker.entities.Vacation;

import java.sql.Date;

public class MainActivity extends AppCompatActivity {

    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        Repository repository = new Repository(getApplication());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, VacationsList.class);
                startActivity(intent);
            }
        });
    }
}