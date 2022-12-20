package com.example.pollingsystem.ui.activeunansweredpolls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.DBHelper;
import com.example.pollingsystem.data.global.LoggedInUserApplication;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.data.model.User;
import com.example.pollingsystem.ui.createpoll.CreatePollActivity;
import com.example.pollingsystem.ui.historypollsactivity.PollsHistoryActivity;
import com.example.pollingsystem.ui.votepoll.VotePollActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public class ActiveUnansweredPollsActivity extends AppCompatActivity implements ActiveUnansweredPollsAdapter.OnPollClickListener {
    private RecyclerView pollRecyclerView;
    private ActiveUnansweredPollsAdapter pollsAdapter;
    private List<Poll> polls;
    private DBHelper dbHelper;

    public interface OnPollClickListener {
        void onPollClick(int position);
    }

    private OnPollClickListener onPollClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_unanswered_polls);
        SQLiteDatabase db = openOrCreateDatabase("PollingSystem",MODE_PRIVATE,null);
        dbHelper = new DBHelper(db);
        dbHelper.Initialize();
        dbHelper.SetDefaultDbData();

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        ctl.setTitleEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Active polls");
        setSupportActionBar(toolbar);

        try {
            polls = dbHelper.GetUnansweredActivePollsByUserId(UUID.randomUUID());
            pollRecyclerView = (RecyclerView)findViewById(R.id.polls);

            pollRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            pollRecyclerView.setItemAnimator(new DefaultItemAnimator());

            // сетирање на кориснички дефиниран адаптер myAdapter (посебна класа)
            pollsAdapter = new ActiveUnansweredPollsAdapter(polls, R.layout.activity_active_unanswered_polls_poll_row, this);
//прикачување на адаптерот на RecyclerView
            pollRecyclerView.setAdapter(pollsAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayout questionForms = findViewById(R.id.questions_linear_layout);
        switch (item.getItemId()) {
            case R.id.history_button:{
                Intent intent = new Intent(this, PollsHistoryActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.create_poll_button:{
                Intent intent = new Intent(this, CreatePollActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    @Override
    public void onPollClick(int position) {
        Poll clickedPoll = polls.get(position);
        String pollId = String.valueOf(clickedPoll.getId());

        Intent intent = new Intent(this, VotePollActivity.class);
        intent.putExtra("pollId", pollId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.active_unanswered_polls_action_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.create_poll_button);
        UUID userId = ((LoggedInUserApplication)getApplication()).getUserId();
        User user = dbHelper.GetUserById(userId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (user.getRoles().stream().anyMatch(x -> x.getName().equals("Admin"))) {
                menuItem.setVisible(true);
            } else {
                menuItem.setVisible(false);
            }
        }
        return true;
    }
}