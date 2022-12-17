package com.example.pollingsystem.ui.votepoll;

import android.content.Context;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.model.Choice;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.data.model.Question;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class VotePollAdapter extends RecyclerView.Adapter<VotePollAdapter.ViewHolder> {
    private List<Question> questions;
    public List<Choice> choices;
    private int rowLayout;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTitle;
        public RadioGroup questionChoices;
        public ViewHolder(View itemView) {
            super(itemView);
            questionTitle = (TextView) itemView.findViewById(R.id.questionTitle);
            questionChoices = (RadioGroup) itemView.findViewById(R.id.questionChoices);
        }
    }
    // конструктор
    public VotePollAdapter(List<Question> questions, int rowLayout, Context context) {
        this.questions = questions;
        this.choices = new LinkedList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questions.forEach(x -> {
                this.choices.addAll(x.getChoices());
            });
        }
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
        Question entry = questions.get(i);
        viewHolder.questionTitle.setText(entry.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            entry.getChoices().forEach(x -> {
                RadioButton rb = new RadioButton(mContext);
                rb.setText(x.getName());
                viewHolder.questionChoices.addView(rb);
            });
        }
    }
    // Пресметка на големината на податочното множество (повикано од layout manager)
    @Override
    public int getItemCount() {
        return questions == null ? 0 : questions.size();
    }
}