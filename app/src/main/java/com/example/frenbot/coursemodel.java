package com.example.frenbot;

public class coursemodel {
    String course;
    String id;
    String instructor;


    public coursemodel(String course, String id, String instructor) {
        this.course = course;
        this.id = id;
        this.instructor = instructor;
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

}
