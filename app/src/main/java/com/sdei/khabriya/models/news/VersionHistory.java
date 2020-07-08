
package com.sdei.khabriya.models.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionHistory {

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("href")
    @Expose
    private String href;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
