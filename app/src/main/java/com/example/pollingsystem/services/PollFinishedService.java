package com.example.pollingsystem.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.DBHelper;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.ui.activeunansweredpolls.ActiveUnansweredPollsActivity;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PollFinishedService extends Service {
    private static final String CHANNEL_ID = "stefan";

    public PollFinishedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            private static final int NOTIFICATION_ID = 0;

            @Override
            public void run() {
                SQLiteDatabase db = openOrCreateDatabase("PollingSystem", MODE_PRIVATE, null);
                DBHelper dbHelper = new DBHelper(db);
                dbHelper.Initialize();
                dbHelper.SetDefaultDbData();
                try {
                    List<Poll> polls = dbHelper.GetUnansweredActivePollsByUserId(UUID.randomUUID());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        polls.forEach(x -> {
                            Date startDate = x.getStartDate();
                            Integer durationInMinutes = x.getDurationInMinutes();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                if(Date.from(Instant.now()).after(new Date(startDate.getTime() + durationInMinutes * 60 * 1000- 60000))){
                                    // Build the notification
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(PollFinishedService.this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.notification_icon)
                                            .setContentTitle("Poll Published")
                                            .setContentText("Your poll has been published successfully.")
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setAutoCancel(true);

                                    // Show the notification
                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PollFinishedService.this);
                                    notificationManager.notify(NOTIFICATION_ID, builder.build());

                                    Intent intent = new Intent(PollFinishedService.this, ActiveUnansweredPollsActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

// Schedule the task to run every 60 seconds
        scheduledExecutorService.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);

        return flags;
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