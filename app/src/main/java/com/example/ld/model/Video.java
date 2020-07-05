package com.example.ld.model;

public class Video {

    int id;
    String namaVideo, thumbnail, file;

    public Video() {
    }

    public Video(int id, String namaVideo, String thumbnail, String file) {
        this.id = id;
        this.namaVideo = namaVideo;
        this.thumbnail = thumbnail;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaVideo() {
        return namaVideo;
    }

    public void setNamaVideo(String namaVideo) {
        this.namaVideo = namaVideo;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
