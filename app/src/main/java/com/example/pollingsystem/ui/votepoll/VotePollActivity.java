package com.example.pollingsystem.ui.votepoll;

import static android.Manifest.permission.*;
import static com.google.android.gms.location.Priority.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.DBHelper;
import com.example.pollingsystem.data.global.LoggedInUserApplication;
import com.example.pollingsystem.data.model.Choice;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.data.model.User;
import com.example.pollingsystem.data.model.UserChoice;
import com.example.pollingsystem.ui.activeunansweredpolls.ActiveUnansweredPollsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class VotePollActivity extends AppCompatActivity {
    private RecyclerView votePollRecyclerView;
    private VotePollAdapter votePollAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_poll);

        SQLiteDatabase db = openOrCreateDatabase("PollingSystem", MODE_PRIVATE, null);
        dbHelper = new DBHelper(db);
        dbHelper.Initialize();
        dbHelper.SetDefaultDbData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button_white);
            actionBar.setTitle("Vote poll");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            Intent intent = getIntent();
            UUID pollId = UUID.fromString(intent.getStringExtra("pollId"));
            Poll poll = dbHelper.GetPollById(pollId);
            TextView pollTitle = (TextView) findViewById(R.id.pollTitle);
            pollTitle.setText(poll.getName());
            votePollRecyclerView = (RecyclerView) findViewById(R.id.question);

            votePollRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            votePollRecyclerView.setItemAnimator(new DefaultItemAnimator());

            // сетирање на кориснички дефиниран адаптер myAdapter (посебна класа)
            votePollAdapter = new VotePollAdapter(poll.getQuestions(), R.layout.activity_vote_poll_question_row, this);
//прикачување на адаптерот на RecyclerView
            votePollRecyclerView.setAdapter(votePollAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button submitVote = (Button) findViewById(R.id.submitVote);
        submitVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView questions = (RecyclerView) findViewById(R.id.question);
                for (int i = 0; i < questions.getChildCount(); i++) {
                    VotePollAdapter.ViewHolder holder = (VotePollAdapter.ViewHolder) questions.findViewHolderForAdapterPosition(i);
                    RadioGroup rg = holder.itemView.findViewById(R.id.questionChoices);
                    int buttonId = rg.getCheckedRadioButtonId();
                    Choice choice = votePollAdapter.choices.get(buttonId - 1);
                    if ((ActivityCompat.checkSelfPermission(
                            VotePollActivity.this,
                            ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                            (ActivityCompat.checkSelfPermission(VotePollActivity.this,
                                    ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(VotePollActivity.this, new String[]{
                                ACCESS_COARSE_LOCATION,
                                ACCESS_FINE_LOCATION}, 69);
                        return;
                    }
                    final Location[] voteLocation = new Location[1];
                    UUID userId = ((LoggedInUserApplication) getApplication()).getUserId();
                    User user = dbHelper.GetUserById(userId);
                    fusedLocationClient.getCurrentLocation(PRIORITY_LOW_POWER, null).addOnSuccessListener(
                            new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    voteLocation[0] = location;
                                    UserChoice userChoice = new UserChoice(
                                            user.getId(),
                                            choice.getId(),
                                            new Date(),
                                            voteLocation[0]);
                                    dbHelper.SaveUserChoice(userChoice);
                                    Intent intent = new Intent(VotePollActivity.this, ActiveUnansweredPollsActivity.class);
                                    startActivity(intent);
                                }
                            }
                    );

                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Integer> selectedChoices = (ArrayList<Integer>) votePollAdapter.getSelectedChoices();
        outState.putParcelableArrayList("questions", selectedChoices);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedChoices = savedInstanceState.getParcelableArrayList("questions");
    }
}
