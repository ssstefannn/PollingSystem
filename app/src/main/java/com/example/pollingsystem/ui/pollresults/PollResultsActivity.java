package com.example.pollingsystem.ui.pollresults;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.DBHelper;
import com.example.pollingsystem.data.model.Choice;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.data.model.Question;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PollResultsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private LinearLayout questionLayout;
    private List<Double> latitudes;
    private List<Double> longitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_results);

        SQLiteDatabase db = openOrCreateDatabase("PollingSystem", MODE_PRIVATE, null);
        dbHelper = new DBHelper(db);
        dbHelper.Initialize();
        dbHelper.SetDefaultDbData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Poll results");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button_white);
            actionBar.setTitle("Poll results");
        }

        questionLayout = (LinearLayout) findViewById(R.id.question_layout);

        Poll poll = null;
        try {
            Intent intent = getIntent();
            UUID pollId = UUID.fromString(intent.getStringExtra("pollId"));
            poll = dbHelper.GetPollById(pollId);
            // Set the poll title
            TextView pollTitleTextView = (TextView) findViewById(R.id.poll_title);
            pollTitleTextView.setText(poll.getName());

            // Inflate and add the question views to the layout
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Question question : poll.getQuestions()) {
                // Inflate the question item layout
                View questionView = inflater.inflate(R.layout.activity_poll_results_question_item, questionLayout, false);

                // Set the question name
                TextView questionNameTextView = (TextView) questionView.findViewById(R.id.question_name);
                questionNameTextView.setText(question.getName());

                // Set the choices and vote counts
                LinearLayout choicesLayout = (LinearLayout) questionView.findViewById(R.id.choices_layout);
                for (Choice choice : question.getChoices()) {
                    // Inflate the choice layout
                    View choiceView = inflater.inflate(R.layout.activity_poll_results_choice_item, choicesLayout, false);

                    // Set the choice text
                    TextView choiceTextView = (TextView) choiceView.findViewById(R.id.choice_text);
                    choiceTextView.setText(choice.getName());

                    // Set the vote count
                    TextView voteCountTextView = (TextView) choiceView.findViewById(R.id.vote_count);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        voteCountTextView.setText(String.valueOf(choice.getUserChoices().stream().count()));
                    }

                    // Add the choice view to the choices layout
                    choicesLayout.addView(choiceView);
                }

                // Add the question view to the question layout
                questionLayout.addView(questionView);

                latitudes = new LinkedList<>();
                longitudes = new LinkedList<>();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    poll.getQuestions().forEach(x -> {
                        x.getChoices().forEach(y -> {
                            y.getUserChoices().forEach(z -> {
                                latitudes.add(z.getSubmittedIn().getLatitude());
                                longitudes.add(z.getSubmittedIn().getLongitude());
                            });
                        });
                    });
                }

                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callback);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                for (int i = 0; i < latitudes.stream().count(); i++) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitudes.get(i), longitudes.get(i))));
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.poll_results_action_bar, menu);
        return true;
    }
}
