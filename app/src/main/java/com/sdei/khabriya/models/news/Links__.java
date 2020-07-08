
package com.sdei.khabriya.models.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Links__ {

    @SerializedName("self")
    @Expose
    private List<Self__> self = null;
    @SerializedName("collection")
    @Expose
    private List<Collection__> collection = null;
    @SerializedName("about")
    @Expose
    private List<About_> about = null;
    @SerializedName("wp:post_type")
    @Expose
    private List<WpPostType> wpPostType = null;
    @SerializedName("curies")
    @Expose
    private List<Cury_> curies = null;

    public List<Self__> getSelf() {
        return self;
    }

    public void setSelf(List<Self__> self) {
        this.self = self;
    }

    public List<Collection__> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection__> collection) {
        this.collection = collection;
    }

    public List<About_> getAbout() {
        return about;
    }

    public void setAbout(List<About_> about) {
        this.about = about;
    }

    public List<WpPostType> getWpPostType() {
        return wpPostType;
    }

    public void setWpPostType(List<WpPostType> wpPostType) {
        this.wpPostType = wpPostType;
    }

    public List<Cury_> getCuries() {
        return curies;
    }

    public void setCuries(List<Cury_> curies) {
        this.curies = curies;
    }

}
