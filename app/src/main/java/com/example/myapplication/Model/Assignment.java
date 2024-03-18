package com.example.myapplication.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Assignment data type, is NOT implemented, and needs to implement comparable
 */
public class Assignment implements Comparable<Assignment> {
    private int id;
    private String title;
    private String dueDate;
    private String className;
    private int status;

    public Assignment(int id, String title, String dueDate, String className, int status) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.className = className;
        this.status = status;
    }

    /**
     * The compareTo method implementing sorting by status
     * @param assignment the object to be compared.
     * @return
     */
    public int compareTo(Assignment assignment) {
        if (assignment.getStatus() > status) {
            return -1;
        } else if (assignment.getStatus() == status) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Essentially another version of the compareTo method but to sort by className
     * @param assignment the assignment to compare with
     * @return
     */
    public int courseSort(Assignment assignment) {
        if (assignment.getClassName().equals(className)) {
            return 0;
        } else if (assignment.getClassName().hashCode() > className.hashCode()){
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Essentially the compareTo method but sorts by date
     * @param assignment the assignment being sorted.
     * @return
     */
    public int dateSortAssign(Assignment assignment) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date1 = sdf.parse(dueDate);
            Date date2 = sdf.parse(assignment.getDueDate());
            assert date2 != null;
            if (date2.before(date1)) {
                return 1;
            } else if (date2.equals(date1)) {
                return 0;
            } else {
                return -1;
            }
        } catch(ParseException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public Assignment() {
        //Variables will be initiated to their defaults, so I think this can be left blank.
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
