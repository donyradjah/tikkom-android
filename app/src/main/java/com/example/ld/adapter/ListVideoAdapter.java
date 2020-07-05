package com.example.ld.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ld.R;
import com.example.ld.helper.Utils;
import com.example.ld.model.Video;

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
        String dirPath = Utils.getRootDirPath(context);
        String pathName = dirPath + "/" + video.getThumbnail();
        Drawable d = Drawable.createFromPath(pathName);
        videoHolder.parent.setBackground(d);
        videoHolder.tvNamaVideo.setText("" + video.getNamaVideo());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parent;
        TextView tvNamaVideo;

        public VideoHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.parent);
            tvNamaVideo = view.findViewById(R.id.tvNamaVideo);
        }

    }
}
