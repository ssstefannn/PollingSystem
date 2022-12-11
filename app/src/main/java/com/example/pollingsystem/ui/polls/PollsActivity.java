package com.example.pollingsystem.ui.polls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.DBHelper;
import com.example.pollingsystem.data.model.Poll;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public class PollsActivity extends AppCompatActivity {
    private RecyclerView pollRecyclerView;
    private PollsAdapter pollsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = openOrCreateDatabase("PollingSystem",MODE_PRIVATE,null);
        DBHelper dbHelper = new DBHelper(db);
        dbHelper.Initialize();
        dbHelper.SetDefaultDbData();

        try {
            List<Poll> polls = dbHelper.GetUnansweredActivePollsByUserId(UUID.randomUUID());
            pollRecyclerView = (RecyclerView)findViewById(R.id.polls);

            pollRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            pollRecyclerView.setItemAnimator(new DefaultItemAnimator());

            // сетирање на кориснички дефиниран адаптер myAdapter (посебна класа)
            pollsAdapter = new PollsAdapter(polls, R.layout.poll_row, this);
//прикачување на адаптерот на RecyclerView
            pollRecyclerView.setAdapter(pollsAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}