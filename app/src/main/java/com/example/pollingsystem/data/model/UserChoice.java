package com.example.pollingsystem.data.model;

import android.location.Location;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

public class UserChoice extends BaseModel {
    private UUID userId;
    private UUID choiceId;
    private Date submittedOn;
    private Location submittedIn;

    public UserChoice(UUID userId, UUID choiceId, Date submittedOn, Location submittedIn) {
        super();
        this.userId = userId;
        this.choiceId = choiceId;
        this.submittedOn = submittedOn;
        this.submittedIn = submittedIn;
    }

    public UserChoice(UUID id, UUID userId, UUID choiceId, Date submittedOn, Location submittedIn) {
        super(id);
        this.userId = userId;
        this.choiceId = choiceId;
        this.submittedOn = submittedOn;
        this.submittedIn = submittedIn;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public Location getSubmittedIn() {
        return submittedIn;
    }

    public void setSubmittedIn(Location submittedIn) {
        this.submittedIn = submittedIn;
    }

    public UUID getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(UUID choiceId) {
        this.choiceId = choiceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
