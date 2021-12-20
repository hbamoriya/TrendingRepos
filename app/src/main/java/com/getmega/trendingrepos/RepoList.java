package com.getmega.trendingrepos;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

public class RepoList {
    private String name;
    private String description;
    private String language;
    private String languageColor;
    private String stars;
    private String forks;

    public Image getImage() {
        return image;
    }

    private Image image;
    @SerializedName("body")

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getLanguageColor() {
        return languageColor;
    }

    public String getStars() {
        return stars;
    }

    public String getForks() {
        return forks;
    }



}
