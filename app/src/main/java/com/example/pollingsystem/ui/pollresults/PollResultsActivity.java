package com.example.pollingsystem.ui.pollresults;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.DBHelper;
import com.example.pollingsystem.data.model.Choice;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.data.model.Question;
import com.example.pollingsystem.ui.resultsmap.MapsFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import kotlin.TuplesKt;

public class PollResultsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private LinearLayout questionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_results);

        SQLiteDatabase db = openOrCreateDatabase("PollingSystem", MODE_PRIVATE, null);
        dbHelper = new DBHelper(db);
        dbHelper.Initialize();
        dbHelper.SetDefaultDbData();

        // Find the LinearLayout in the layout
        questionLayout = (LinearLayout) findViewById(R.id.question_layout);


        // Retrieve the poll data
        Poll poll = null;
        try {
            poll = dbHelper.GetPollById(UUID.fromString("4233ee29-d06f-495c-823d-c83d9e89f68f"));
            // Set the poll title
            TextView pollTitleTextView = (TextView) findViewById(R.id.poll_title);
            pollTitleTextView.setText(poll.getName());

            // Inflate and add the question views to the layout
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Question question : poll.getQuestions()) {
                // Inflate the question item layout
                View questionView = inflater.inflate(R.layout.question_item, questionLayout, false);

                // Set the question name
                TextView questionNameTextView = (TextView) questionView.findViewById(R.id.question_name);
                questionNameTextView.setText(question.getName());

                // Set the choices and vote counts
                LinearLayout choicesLayout = (LinearLayout) questionView.findViewById(R.id.choices_layout);
                for (Choice choice : question.getChoices()) {
                    // Inflate the choice layout
                    View choiceView = inflater.inflate(R.layout.choice_item, choicesLayout, false);

                    // Set the choice text
                    TextView choiceTextView = (TextView) choiceView.findViewById(R.id.choice_text);
                    choiceTextView.setText(choice.getName());

                    // Set the vote count
                    TextView voteCountTextView = (TextView) choiceView.findViewById(R.id.vote_count);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //voteCountTextView.setText(String.valueOf(choice.getUserChoices().stream().count()));
                        voteCountTextView.setText(String.valueOf(1000));
                    }

                    // Add the choice view to the choices layout
                    choicesLayout.addView(choiceView);
                }

                // Add the question view to the question layout
                questionLayout.addView(questionView);

                // In the activity:

                LinkedList<Double> latitudes = new LinkedList<>();
                LinkedList<Double> longitudes = new LinkedList<>();

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

                Bundle bundle = new Bundle();

                bundle.putSerializable("latitudes", latitudes);
                bundle.putSerializable("longitudes", longitudes);

                MapsFragment fragment = new MapsFragment();

                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().add(R.id.map, fragment).commit();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
