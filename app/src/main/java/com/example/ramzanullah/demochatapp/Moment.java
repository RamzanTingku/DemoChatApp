package com.example.ramzanullah.demochatapp;

/**
 * Created by Ramzan Ullah on 10/2/2017.
 */

public class Moment {

    private String image;
    private String title;
    private String desc;

    public Moment() {
    }

    public Moment(String image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}

