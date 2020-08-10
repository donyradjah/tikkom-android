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
import com.example.ld.model.DaftarMateri;
import com.example.ld.model.Materi;

import java.util.ArrayList;

public class ListDaftarMateriAdapter extends RecyclerView.Adapter<ListDaftarMateriAdapter.DaftarMateriHolder> {
    Context context;
    ArrayList<DaftarMateri> materis;
    Activity activity;

    public ListDaftarMateriAdapter(Context context, ArrayList<DaftarMateri> materis, Activity activity) {
        this.context = context;
        this.materis = materis;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DaftarMateriHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daftar_isi, viewGroup, false);
        return new DaftarMateriHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarMateriHolder materiHolder, int i) {
        DaftarMateri materi = materis.get(i);

        materiHolder.tvNamaMateri.setText(materi.getJudul() + "");
        materiHolder.tvHalMateri.setText("Halaman : " + materi.getHalaman());

    }

    @Override
    public int getItemCount() {
        return materis.size();
    }

    public class DaftarMateriHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMateri;
        TextView tvHalMateri;
        View viewCard;


        public DaftarMateriHolder(@NonNull View itemView) {
            super(itemView);
            viewCard = itemView;
            tvNamaMateri = itemView.findViewById(R.id.tvNamaMateri);
            tvHalMateri = itemView.findViewById(R.id.tvHalMateri);
        }
    }
}
