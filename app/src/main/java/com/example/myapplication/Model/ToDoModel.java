package com.example.myapplication.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple class that defines the TODOModel data type, just has getters and setters for all our variables.
 *
 */
public class ToDoModel implements Comparable<ToDoModel> {
    private int id;
    private int status;
    private String dueDate;
    private String task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    /**
     * the compareTo method that sorts based on status
     * @param toDoModel the object to be compared.
     * @return
     */
    public int compareTo(ToDoModel toDoModel) {
        if (toDoModel.getStatus() > status) {
            return -1;
        } else if (toDoModel.getStatus() == status) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Essentiall the compareTo method but it sorts by dueDate instead
     * @param toDoModel the object to be compared
     * @return
     */
    public int dateSort(ToDoModel toDoModel) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date1 = sdf.parse(dueDate);
            Date date2 = sdf.parse(toDoModel.getDueDate());
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
}
