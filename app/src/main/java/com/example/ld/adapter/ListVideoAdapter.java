package com.example.ld.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.example.ld.R;
import com.example.ld.activity.DetailVideoActivity;
import com.example.ld.helper.UrlApi;
import com.example.ld.helper.Utils;
import com.example.ld.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.VideoHolder> {
    Context context;
    ArrayList<Video> videos;
    Activity activity;

    public ListVideoAdapter(Activity activity, Context context, ArrayList<Video> videos) {
        this.context = context;
        this.videos = videos;
        this.activity = activity;
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

        videoHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = UrlApi.BASE_URL_API + "public/upload/video/" + video.getFile();
//
//                Intent intent = new Intent(activity, DetailVideoActivity.class);
//
//                intent.putExtra("judul", video.getNamaVideo());
//                intent.putExtra("url", url);
//                activity.startActivity(intent);

                String url = UrlApi.BASE_URL_API + "public/upload/video/" + video.getFile();
                activity.startActivity(PlayerActivity.getVideoPlayerIntent(activity,
                        url,
                        video.getNamaVideo()));

            }
        });
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
