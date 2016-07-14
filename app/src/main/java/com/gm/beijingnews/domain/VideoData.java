package com.gm.beijingnews.domain;

/**
 * Created by Administrator on 2016/7/9.
 */
public class VideoData {
    private String title;
    private String videoUrl;

    public VideoData() {
    }

    public VideoData(String title, String videoUrl) {
        this.title = title;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
