package com.example.ld.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ld.R;
import com.example.ld.helper.UrlApi;
import com.example.ld.helper.Utils;
import com.example.ld.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.VideoHolder> {
    Context context;
    ArrayList<Video> videos;

    public ListVideoAdapter(Context context, ArrayList<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup, false);
        return new VideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder videoHolder, int i) {
        Video video = videos.get(i);
        String url = UrlApi.BASE_URL_API + "public/upload/thumbnail-video/" + video.getThumbnail();

        Picasso.get().load(url).into(videoHolder.icMateri);

        videoHolder.tvNamaVideo.setText("" + video.getNamaVideo());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        ImageView icMateri;
        TextView tvNamaVideo;

        public VideoHolder(View view) {
            super(view);
            icMateri = view.findViewById(R.id.icMateri);
            tvNamaVideo = view.findViewById(R.id.tvNamaVideo);
        }

    }
}
