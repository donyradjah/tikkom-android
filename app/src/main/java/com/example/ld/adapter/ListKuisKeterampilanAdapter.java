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
import com.example.ld.activity.DetailKuisKeterampilanActivity;
import com.example.ld.model.KuisKeterampilan;

import java.util.ArrayList;

public class ListKuisKeterampilanAdapter extends RecyclerView.Adapter<ListKuisKeterampilanAdapter.KuisKeterampilanHolder> {
    Context context;
    ArrayList<KuisKeterampilan> kuisKeterampilans;
    Activity activity;

    public ListKuisKeterampilanAdapter(Context context, ArrayList<KuisKeterampilan> kuisKeterampilans, Activity activity) {
        this.context = context;
        this.kuisKeterampilans = kuisKeterampilans;
        this.activity = activity;
    }

    @NonNull
    @Override
    public KuisKeterampilanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kuis_keterampilan, viewGroup, false);
        return new KuisKeterampilanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KuisKeterampilanHolder kuisKeterampilanHolder, int i) {
        KuisKeterampilan kuisKeterampilan = kuisKeterampilans.get(i);
        String pertanyaan = kuisKeterampilan.getPertanyaan();

        if (pertanyaan.length() > 200) {
            pertanyaan = pertanyaan.substring(0, 200) + " . . . ";
        }

        kuisKeterampilanHolder.tvNomor.setText((i + 1) + "");
        kuisKeterampilanHolder.tvPertanyaaan.setText(pertanyaan);

        kuisKeterampilanHolder.viewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KuisKeterampilan kuisKeterampilan = kuisKeterampilans.get(i);

                Intent detailMateri = new Intent(context, DetailKuisKeterampilanActivity.class);
                detailMateri.putExtra("id", kuisKeterampilan.getId());

                activity.startActivity(detailMateri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kuisKeterampilans.size();
    }

    public class KuisKeterampilanHolder extends RecyclerView.ViewHolder {
        TextView tvNomor;
        TextView tvPertanyaaan;
        View viewCard;

        public KuisKeterampilanHolder(View view) {
            super(view);
            viewCard = view;
            tvNomor = view.findViewById(R.id.tvNomor);
            tvPertanyaaan = view.findViewById(R.id.tvPertanyaaan);
        }

    }
}
