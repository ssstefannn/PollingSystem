package com.example.pollingsystem.ui.votepoll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pollingsystem.R;
import com.example.pollingsystem.data.model.Choice;
import com.example.pollingsystem.data.model.Question;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class VotePollAdapter extends RecyclerView.Adapter<VotePollAdapter.ViewHolder> {
    public List<Question> questions;
    public List<Choice> choices;
    public List<Integer> selectedChoices;

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
        this.selectedChoices = new LinkedList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questions.forEach(x -> {
                selectedChoices.add(-1);
            });
        }
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
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        final Question entry =  questions.get(i);
        viewHolder.questionTitle.setText(entry.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            entry.getChoices().forEach(x -> {
                RadioButton rb = new RadioButton(mContext);
                rb.setText(x.getName());
                viewHolder.questionChoices.addView(rb);
            });
        }

        // Get the index of the radio button that should be checked
        int checkedIndex = selectedChoices.get(i);
        if(checkedIndex != -1){
            // Get a reference to the radio button at the checked index
            RadioButton checkedButton = (RadioButton) viewHolder.questionChoices.getChildAt(checkedIndex);
            // Set the checked state of the radio button
            checkedButton.setChecked(true);
        }

        viewHolder.questionChoices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedChoices.set(i, viewHolder.questionChoices.indexOfChild(group.findViewById(checkedId)));
            }
        });
    }


    // Пресметка на големината на податочното множество (повикано од layout manager)
    @Override
    public int getItemCount() {
        return questions == null ? 0 : questions.size();
    }

    public List<Integer> getSelectedChoices(){
        return selectedChoices;
    }

    public void setSelectedChoices(List<Integer> selectedChoices){
        this.selectedChoices = selectedChoices;
    }
}