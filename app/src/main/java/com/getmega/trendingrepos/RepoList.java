package com.getmega.trendingrepos;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

public class RepoList {
    private String author;
    private String name;
    private String description;
    private String language;
    private String languageColor;
    private String stars;
    private String forks;
    private boolean expanded;

    public RepoList(String author, String name, String description, String language, String languageColor, String stars, String forks, RepoList getItem, Image image) {
        this.author = author;
        this.name = name;
        this.description = description;
        this.language = language;
        this.languageColor = languageColor;
        this.stars = stars;
        this.forks = forks;
        this.getItem = getItem;
        this.image = image;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public RepoList getGetItem() {
        return getItem;
    }

    private RepoList getItem;

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


    public String getAuthor() {
        return author;
    }
}
