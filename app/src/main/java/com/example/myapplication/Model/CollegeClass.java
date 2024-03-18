package com.example.myapplication.Model;

/**
 * CollegeClass data type, is NOT implemented, and needs to implement comparable
 */
public class CollegeClass {
    private int id;
    private String professor;
    private String classSection;
    private String building;
    private String roomNumber;
    private String time;
    private String classDates;
    private String name;
    public CollegeClass(String name, String classDates, String time, String professor, String classSection,
                        String building, String roomNumber) {
        this.name = name;
        this.classDates = classDates;
        this.time = time;
        this.professor = professor;
        this.classSection = classSection;
        this.building = building;
        this.roomNumber = roomNumber;
    }
    public CollegeClass() {
        //Variables will be initiated to their defaults, so I think this can be left blank.
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setClassDates(String classDates) {
        this.classDates = classDates;
    }
    public String getClassDates() {
        return classDates;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }
    public void setProfessor(String professor) {
        this.professor = professor;
    }
    public String getProfessor() {
        return professor;
    }
    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }
    public String getClassSection() {
        return classSection;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public String getBuilding() {
        return building;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }





}
