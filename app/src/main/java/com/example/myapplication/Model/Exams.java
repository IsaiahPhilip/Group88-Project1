package com.example.myapplication.Model;

/**
 * Exams data type, is NOT implemented, and needs to implement comparable
 */
public class Exams {
    private String title;
    private String dueDate;
    private String time;
    private int status;
    private int id;
    private String className;
    private String location;
    public Exams (String title, String dueDate, String time, String location, String className, int status, int id) {
        this.title = title;
        this.dueDate = dueDate;
        this.time = time;
        this.location = location;
        this.className = className;
        this.status = status;
        this.id = id;
    }
    public Exams() {
        //Variables will be initiated to their defaults, so I think this can be left blank.
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getClassName() {
        return className;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTime(String time) {
        this.dueDate = time;
    }
    public String getTime() {
        return dueDate;
    }
    public void setDate(String date) {
        this.time = date;
    }
    public String getDate() {
        return time;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return location;
    }
}
