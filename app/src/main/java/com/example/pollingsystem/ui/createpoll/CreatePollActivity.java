package com.example.pollingsystem.ui.createpoll;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.DBHelper;
import com.example.pollingsystem.data.global.LoggedInUserApplication;
import com.example.pollingsystem.data.model.Choice;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.data.model.Question;
import com.example.pollingsystem.ui.activeunansweredpolls.ActiveUnansweredPollsActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class CreatePollActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "idk";
    private static final int NOTIFICATION_ID = 0;
    private LinearLayout questionsLinearLayout;
    private Button addQuestionButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        questionsLinearLayout = findViewById(R.id.questions_linear_layout);
        addQuestionButton = findViewById(R.id.add_question_button);

        // Add the default question
        addQuestion();

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
            }
        });

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.ctl);
        ctl.setTitleEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Create a new poll");
        setSupportActionBar(toolbar);

        SQLiteDatabase db = openOrCreateDatabase("PollingSystem", MODE_PRIVATE, null);
        dbHelper = new DBHelper(db);
        dbHelper.Initialize();
        dbHelper.SetDefaultDbData();

        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_poll_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayout questionForms = findViewById(R.id.questions_linear_layout);
        switch (item.getItemId()) {
            case R.id.action_publish_poll:
                EditText pollTitleEditText = (EditText) findViewById(R.id.poll_title_edit_text);
                String pollTitle = pollTitleEditText.getText().toString();
                EditText pollDurationInMinutesEditText = (EditText) findViewById(R.id.poll_duration);
                Integer durationInMinutes = Integer.parseInt(pollDurationInMinutesEditText.getText().toString());
                UUID userId = ((LoggedInUserApplication) getApplication()).getUserId();
                Poll poll = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    poll = new Poll(userId, pollTitle, Date.from(Instant.now()), durationInMinutes);
                }
                dbHelper.SavePoll(poll);
                for (int i = 0; i < questionForms.getChildCount(); i++) {
                    View childView = questionForms.getChildAt(i);
                    LinearLayout questionForm = (LinearLayout) childView;
                    EditText questionEditText = (EditText) questionForm.getChildAt(0);
                    String questionTitle = questionEditText.getText().toString();
                    Question question = new Question(poll.getId(), questionTitle);
                    dbHelper.SaveQuestion(question);
                    for (int j = 1; j < questionForm.getChildCount() - 1; j++) {
                        EditText choiceEditText = (EditText) questionForm.getChildAt(j);
                        String choiceText = choiceEditText.getText().toString();
                        Choice choice = new Choice(question.getId(), choiceText);
                        dbHelper.SaveChoice(choice);
                    }
                }

                Intent intent = new Intent(this, ActiveUnansweredPollsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                // Build the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("New poll")
                        .setContentText("New poll has been published.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .setAutoCancel(true);

                // Show the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(NOTIFICATION_ID, builder.build());

                Intent intentT = new Intent(this, ActiveUnansweredPollsActivity.class);
                startActivity(intentT);
                 return true;
        }

        return false;
    }

    private void addQuestion() {
        LinearLayout questionLinearLayout = new LinearLayout(this);
        questionLinearLayout.setOrientation(LinearLayout.VERTICAL);

        EditText questionEditText = new EditText(this);
        questionEditText.setHint("Enter question title");
        questionEditText.setTextSize(25);
        questionLinearLayout.addView(questionEditText);

        Button addOptionButton = new Button(this);
        addOptionButton.setText("Add option");
        addOptionButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
        questionLinearLayout.addView(addOptionButton);

        // Default 3 options
        addOption(questionLinearLayout, 1);
        addOption(questionLinearLayout, 1);
        addOption(questionLinearLayout, 1);

        addOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOption(questionLinearLayout, 1);
            }
        });

        questionsLinearLayout.addView(questionLinearLayout);
    }

    private void addOption(LinearLayout questionLinearLayout, int layoutWeight) {
        EditText optionEditText = new EditText(this);
        optionEditText.setHint("Enter option title");
        optionEditText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, layoutWeight));
        questionLinearLayout.addView(optionEditText, questionLinearLayout.getChildCount() - 1);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
