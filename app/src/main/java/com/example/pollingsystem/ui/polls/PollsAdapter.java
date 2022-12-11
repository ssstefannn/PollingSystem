package com.example.pollingsystem.ui.polls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.model.Poll;

import java.util.List;

public class PollsAdapter extends RecyclerView.Adapter<PollsAdapter.ViewHolder> {
private List<Poll> polls;
private int rowLayout;
private Context mContext;

// Референца на views за секој податок
// Комплексни податоци може да бараат повеќе views per item
// Пристап до сите views за податок се дефинира во view holder

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView pollTitle;
    public TextView pollStatus;
    public ViewHolder(View itemView) {
        super(itemView);
        pollTitle = (TextView) itemView.findViewById(R.id.pollTitle);
        pollStatus = (TextView) itemView.findViewById(R.id.pollStatus);
    }
}
    // конструктор
    public PollsAdapter(List<Poll> polls, int rowLayout, Context context) {
        this.polls = polls;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }
    // Креирање нови views (повикано од layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    // Замена на содржината во view (повикано од layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Poll entry = polls.get(i);
        viewHolder.pollTitle.setText(entry.getName());
//        viewHolder.pollTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView tv = (TextView) v;
//                Toast.makeText(mContext, tv.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
        viewHolder.pollStatus.setText("x minutes left");
    }
    // Пресметка на големината на податочното множество (повикано од layout manager)
    @Override
    public int getItemCount() {
        return polls == null ? 0 : polls.size();
    }
}