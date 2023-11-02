package com.example.frenbot;

public class eventmodel {
    String title;
    String time;
    String place;
    String uuid;


    public eventmodel(String title, String time, String place, String uuid) {
        this.title = title;
        this.time = time;
        this.place = place;
        this.uuid = uuid;
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
