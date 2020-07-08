/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sdei.khabriya.models;

import android.os.Parcel;

public class ViewModel {
    private String title;
    private String coverImage;
    private String content;
    private String link;



    private String authorImage;
    private String authorName;
    private String authorDesc;
    private String authorEmail;
    private String authorID;
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ViewModel(String title, String coverImage, String authorImage,
                     String authorName,
                     String authorDesc,
                     String authorEmail,
                     String authorID, String content,
                     String detailLink,
                     String date
    ) {
        this.title = title;
        this.coverImage = coverImage;
        this.content = content;

        this.authorImage = authorImage;
        this.authorName = authorName;
        this.authorDesc = authorDesc;
        this.authorEmail = authorEmail;
        this.authorID = authorID;

        this.link = detailLink;

        this.date = date;
    }

    protected ViewModel(Parcel in) {
        title = in.readString();
        coverImage = in.readString();
        authorImage = in.readString();
        authorName = in.readString();
        authorDesc = in.readString();
        authorEmail = in.readString();
        authorID = in.readString();
        link = in.readString();
        date = in.readString();
    }

//    public static final Creator<ViewModel> CREATOR = new Creator<ViewModel>() {
//        @Override
//        public ViewModel createFromParcel(Parcel in) {
//            return new ViewModel(in);
//        }
//
//        @Override
//        public ViewModel[] newArray(int size) {
//            return new ViewModel[size];
//        }
//    };


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return title;
    }

    public String getImage() {
        return coverImage;
    }

    public int describeContents() {
        return 0;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorDesc() {
        return authorDesc;
    }

    public void setAuthorDesc(String authorDesc) {
        this.authorDesc = authorDesc;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(coverImage);
        dest.writeString(authorImage);
        dest.writeString(authorName);
        dest.writeString(authorDesc);
        dest.writeString(authorEmail);
        dest.writeString(authorID);
        dest.writeString(content);
        dest.writeString(link);
        dest.writeString(date);
    }
}
