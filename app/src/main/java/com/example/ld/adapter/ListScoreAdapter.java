package com.example.ld.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ld.R;
import com.example.ld.model.Score;

import java.util.ArrayList;

public class ListScoreAdapter extends RecyclerView.Adapter<ListScoreAdapter.ScoreHolder> {
    Context context;
    ArrayList<Score> scores;
    Activity activity;

    public ListScoreAdapter(Context context, ArrayList<Score> scores, Activity activity) {
        this.context = context;
        this.scores = scores;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_score, viewGroup, false);
        return new ScoreHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder scoreHolder, int i) {
        Score score = scores.get(i);

        scoreHolder.tvScore.setText(score.getScore());
        scoreHolder.tvJam.setText(score.getWaktu());
        scoreHolder.tvBenar.setText("Benar : " + score.getBenar());
        scoreHolder.tvSalah.setText("Salah : " + score.getSalah());
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ScoreHolder extends RecyclerView.ViewHolder {
        TextView tvScore;
        TextView tvJam;
        TextView tvBenar;
        TextView tvSalah;
        View viewCard;

        public ScoreHolder(View view) {
            super(view);
            viewCard = view;
            tvScore = view.findViewById(R.id.tvScore);
            tvJam = view.findViewById(R.id.tvJam);
            tvBenar = view.findViewById(R.id.tvBenar);
            tvSalah = view.findViewById(R.id.tvSalah);
        }

    }
}
