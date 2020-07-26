package com.sdei.khabriya.models.tv;

public class ChannelModel {


    /**
     * id : 148
     * channel_name : Prime Asia
     * add_language : Urdu
     * image : https://tv.khabriya.in/admin/upload/News-300x100-White.png
     * stream_url : http://primeasia.dyndns.tv:8080/Live_web_250/tracks-v1a1/mono.m3u8
     */

    private String id;
    private String channel_name;
    private String add_language;
    private String image;
    private String stream_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getAdd_language() {
        return add_language;
    }

    public void setAdd_language(String add_language) {
        this.add_language = add_language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }
}
