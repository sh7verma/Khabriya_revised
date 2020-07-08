
package com.sdei.khabriya.models.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Embedded {

    @SerializedName("author")
    @Expose
    private List<Author_> author = null;
    @SerializedName("wp:term")
    @Expose
    private List<List<WpTerm_>> wpTerm = null;

    public List<Author_> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author_> author) {
        this.author = author;
    }

    public List<List<WpTerm_>> getWpTerm() {
        return wpTerm;
    }

    public void setWpTerm(List<List<WpTerm_>> wpTerm) {
        this.wpTerm = wpTerm;
    }

}
