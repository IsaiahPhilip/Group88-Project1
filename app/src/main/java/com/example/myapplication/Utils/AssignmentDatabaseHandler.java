package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Model.Assignment;

import java.util.ArrayList;
import java.util.List;

public class AssignmentDatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "assignme" +
            "ntDatabase";
    private static final String ASSIGNMENTS_TABLE = "Assignments";
    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String CLASS_NAME = "class";
    private static final String DUE_DATE = "dueDate";
    private static final String TITLE = "title";
    private static final String CREATE_ASSIGNMENT_TABLE = "CREATE TABLE " + ASSIGNMENTS_TABLE + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT, " + CLASS_NAME +
            " TEXT, " + DUE_DATE + " TEXT, " + STATUS + " INTEGER)";
    private SQLiteDatabase db;
    public AssignmentDatabaseHandler(Context context) {
        super(context, NAME, null, VERSION); //constructer for the method, calls the super constructor
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ASSIGNMENT_TABLE); //when the class is created, it creates an SQLite table based on parameters listed in the string CREATE_TODO_TABLE
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS_TABLE); //drops table if a new table needs to be created
        onCreate(db); //creates table again
    }
    public void openDatabase() {
        db = this.getWritableDatabase(); //opens the writable SQLite database
    }

    /**
     * Inserts a new assignment into the database
     * @param newAssignment the new assignment ot be added
     */
    public void insertAssignment(Assignment newAssignment) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, newAssignment.getTitle()); //Adds a new task to the SQLite database with task as string input
        cv.put(STATUS, 0); //Adds a new task to the SQLite database with Status 0 (uncomplete)
        cv.put(CLASS_NAME, newAssignment.getClassName()); //needs to be implemented
        cv.put(DUE_DATE, newAssignment.getDueDate()); //needs to be implemented


        db.insert(ASSIGNMENTS_TABLE, null, cv); //inserts data we just created into the TODO_TABLE
    }

    /**
     * A special case used to give an item a specific ID when inserted
     * @param newAssignment the assignment to be added
     * @param newID the id to be given to the assignment
     */
    public void insertAssignmentSpecial(Assignment newAssignment, int newID) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, newAssignment.getTitle()); //Adds a new task to the SQLite database with task as string input
        cv.put(STATUS, newAssignment.getStatus()); //Adds a new task to the SQLite database with Status 0 (uncomplete)
        cv.put(CLASS_NAME, newAssignment.getClassName());
        cv.put(DUE_DATE, newAssignment.getDueDate());
        cv.put(ID, newID);


        db.insert(ASSIGNMENTS_TABLE, null, cv); //inserts data we just created into the ASSIGNMENT_TABLE
    }

    /**
     * Gets all the assignments stored in the list.
     * @return
     */

    public List<Assignment> getAllAssignments() { //This method is pretty easy, just gets a list of all assignments to return
        List<Assignment> assignmentList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction(); //if app closes mid change, this lets us store information without it being changed (we don't lose everything)
        try {
            cur = db.query(ASSIGNMENTS_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        Assignment assignment = new Assignment();
                        assignment.setTitle(cur.getString(cur.getColumnIndexOrThrow(TITLE)));//check the method here, OrThrow might not be needed
                        assignment.setDueDate(cur.getString(cur.getColumnIndexOrThrow(DUE_DATE)));
                        assignment.setClassName(cur.getString(cur.getColumnIndexOrThrow(CLASS_NAME)));
                        assignment.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));
                        assignment.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));
                        assignmentList.add(assignment);
                    } while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return assignmentList;
    }

    /**
     * Updates a given assignment already in place
     * @param id the id of the assignment
     * @param dueDate the assignment's due date
     * @param title the assignment's title
     * @param className the assignment's class name
     */
    public void updateAssignment(int id, String dueDate, String title, String className) { //Updates the name of the task
        ContentValues cv = new ContentValues();
        cv.put(DUE_DATE, dueDate);
        cv.put(TITLE, title);
        cv.put(CLASS_NAME, className);
        db.update(ASSIGNMENTS_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    /**
     * Deletes an assignment from the database
     * @param id the id of the assignment to delete
     */
    public void deleteAssignment(int id) { //deletes the assignment
        db.delete(ASSIGNMENTS_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

    /**
     * Updates the status of an assignment
     * @param id the id of the assignment
     * @param status the status of the assignment
     */
    public void updateStatus(int id, int status) { //updates the status of the task (if it is complete or not)
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(ASSIGNMENTS_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }
}
