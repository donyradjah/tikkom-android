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
import com.example.ld.activity.DetailMateriActivity;
import com.example.ld.model.Materi;

import java.util.ArrayList;

public class ListMateriAdapter extends RecyclerView.Adapter<ListMateriAdapter.MateriHolder> {
    Context context;
    ArrayList<Materi> materis;
    Activity activity;

    public ListMateriAdapter(Context context, ArrayList<Materi> materis, Activity activity) {
        this.context = context;
        this.materis = materis;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MateriHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_materi, viewGroup, false);
        return new MateriHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriHolder materiHolder, int i) {
        Materi materi = materis.get(i);

        materiHolder.tvNamaMateri.setText(materi.getNamaMateri() + "");
        materiHolder.viewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Materi materi = materis.get(i);

                Intent detailMateri = new Intent(context, DetailMateriActivity.class);
                detailMateri.putExtra("id", materi.getId());
                detailMateri.putExtra("namaMateri", materi.getNamaMateri());
                detailMateri.putExtra("file", materi.getFile());

                activity.startActivity(detailMateri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materis.size();
    }

    public class MateriHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMateri;
        View viewCard;

        public MateriHolder(View view) {
            super(view);
            viewCard = view;
            tvNamaMateri = view.findViewById(R.id.tvNamaMateri);
        }

    }
}
