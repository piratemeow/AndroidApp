package com.example.frenbot;

public class eventmodel {
    String title;
    String time;
    String place;


    public eventmodel(String title, String time, String place) {
        this.title = title;
        this.time = time;
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }


}
