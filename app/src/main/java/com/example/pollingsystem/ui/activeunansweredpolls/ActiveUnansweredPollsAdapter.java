package com.example.pollingsystem.ui.activeunansweredpolls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.model.Poll;

import java.util.Date;
import java.util.List;

public class ActiveUnansweredPollsAdapter extends RecyclerView.Adapter<ActiveUnansweredPollsAdapter.ViewHolder> {
    private List<Poll> polls;
    private int rowLayout;
    private Context mContext;

    public interface OnPollClickListener {
        void onPollClick(int position);
    }

    private OnPollClickListener onPollClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pollTitle;
        public TextView pollStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            pollTitle = (TextView) itemView.findViewById(R.id.pollTitle);
            pollStatus = (TextView) itemView.findViewById(R.id.pollStatus);
        }
    }

    // constructor
    public ActiveUnansweredPollsAdapter(List<Poll> polls, int rowLayout, OnPollClickListener onPollClickListener) {
        this.polls = polls;
        this.rowLayout = rowLayout;
        this.onPollClickListener = onPollClickListener;
    }

    // create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        Poll entry = polls.get(i);
        viewHolder.pollTitle.setText(entry.getName());
        int durationInMinutes = entry.getDurationInMinutes();
        Date startDate = entry.getStartDate();
        long minutesLeft = (entry.getStartDate().getTime() + durationInMinutes * 60 * 1000 - (new Date()).getTime())/60000;
        viewHolder.pollStatus.setText(minutesLeft + " minutes left");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPollClickListener.onPollClick(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return polls == null ? 0 : polls.size();
    }
}
