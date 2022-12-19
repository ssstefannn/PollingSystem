package com.example.pollingsystem.ui.historypollsactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.model.Poll;

import java.util.List;

public class PollsHistoryAdapter extends RecyclerView.Adapter<PollsHistoryAdapter.ViewHolder> {
    private List<Poll> polls;
    private int rowLayout;
    private Context mContext;

    public interface OnPollClickListener {
        void onPollClick(int position);
    }

    private OnPollClickListener onPollClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pollTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            pollTitle = (TextView) itemView.findViewById(R.id.pollTitle);
        }
    }

    public PollsHistoryAdapter(List<Poll> polls, int rowLayout, OnPollClickListener onPollClickListener) {
        this.polls = polls;
        this.rowLayout = rowLayout;
        this.onPollClickListener = onPollClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        Poll entry = polls.get(i);
        viewHolder.pollTitle.setText(entry.getName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPollClickListener.onPollClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return polls == null ? 0 : polls.size();
    }
}
