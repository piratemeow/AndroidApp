package com.example.frenbot;

public class coursemodel {
    String course;
    String id;
    String instructor;
    String uuid;


    public coursemodel(String course, String id, String instructor, String uuid) {
        this.course = course;
        this.id = id;
        this.instructor = instructor;
        this.uuid = uuid;
    }

    public String getinstructor() {
        return instructor;
    }

    public String getid() {
        return id;
    }

    public String getcourse() {
        return course;
    }

    public String getUuid() {
        return uuid;
    }
}
