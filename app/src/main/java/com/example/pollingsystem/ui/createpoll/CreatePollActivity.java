package com.example.pollingsystem.ui.createpoll;

import static com.example.pollingsystem.R.id.cardView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.pollingsystem.R;
import com.example.pollingsystem.ui.votepoll.VotePollAdapter;

import java.util.LinkedList;
import java.util.List;

public class CreatePollActivity extends AppCompatActivity {
    private RecyclerView createPollRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        createPollRecyclerView = (RecyclerView) findViewById(R.id.create_question_recycler);

        createPollRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        createPollRecyclerView.setItemAnimator(new DefaultItemAnimator());

        List<ConstraintLayout> constraintLayout = new LinkedList<>();
        constraintLayout.add(new ConstraintLayout());


        // сетирање на кориснички дефиниран адаптер myAdapter (посебна класа)
        CreatePollAdapter createPollAdapter = new CreatePollAdapter(R.layout.create_poll_question_row, this);
//прикачување на адаптерот на RecyclerView
        createPollRecyclerView.setAdapter(createPollAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbuttons, menu);
        return super.onCreateOptionsMenu(menu);
    }
}