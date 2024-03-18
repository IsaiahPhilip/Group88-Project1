package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Model.Exams;

import java.util.ArrayList;
import java.util.List;

/**
 * The DatabaseHandler class is the database for the ToDoModel and todoList section of
 * the app. Complicated class so I've annotated decently, ask me about any questions.
 */
public class ExamHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "examDatabase";
    private static final String EXAM_TABLE = "exam";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String STATUS = "status";
    private static final String CLASS_NAME = "className";
    private static final String DUE_DATE = "dueDate";
    private static final String TIME = "time";
    private static final String LOCATION = "location";
    private static final String CREATE_EXAM_TABLE = "CREATE TABLE " + EXAM_TABLE + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT, " + STATUS + " INTEGER, " + CLASS_NAME + " TEXT, " +
            DUE_DATE + " TEXT, " + TIME + " TEXT, " + LOCATION + " TEXT)";
    //^^^^^^^^This string above is the initiation of the SQLite table. Essentially the purple purple
    //comments are the names of the columns (except TODO_TABLE, this is the name of the table)
    //Make sure these are EXACT before emulating or you will run into problems that I can explain
    private SQLiteDatabase db;

    /**
     * Constructor for the examHandler
     * @param context the context passed into the handler
     */

    public ExamHandler(Context context) {
        super(context, NAME, null, VERSION); //constructer for the method, calls the super constructor
    }

    /**
     * Runs when the database is created, creates the SQLite table
     * @param db The database.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EXAM_TABLE); //when the class is created, it creates an SQLite table based on parameters listed in the string CREATE_TODO_TABLE
    }

    /**
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EXAM_TABLE); //drops table if a new table needs to be created
        onCreate(db); //creates table again
    } //^^^This method DOES NOT work on startup, errors from column names and such need to be fixed in the cache of the emulator

    /**
     * Opens the database
     */
    public void openDatabase() {
        db = this.getWritableDatabase(); //opens the writable SQLite database
    }

    /**
     * Inserts a new exam into the database
     * @param exam the exam to be inserted
     */
    public void insertExam(Exams exam) {
        ContentValues cv = new ContentValues();
        cv.put(TITLE, exam.getTitle()); //Adds a new task to the SQLite database with task as string input
        cv.put(CLASS_NAME, exam.getClassName());
        cv.put(DUE_DATE, exam.getDate());
        cv.put(TIME, exam.getTime());
        cv.put(LOCATION, exam.getLocation());
        cv.put(STATUS, 0); //Adds a new task to the SQLite database with Status 0 (uncomplete)

        db.insert(EXAM_TABLE, null, cv); //inserts data we just created into the TODO_TABLE
    }

    /**
     * Gets a list view of all exams stored in the database.
     * @return returns the list of all exams stored in the db
     */

    public List<Exams> getAllExams() { //This method is pretty easy, just gets a list of all tasks to return
        List<Exams> examList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction(); //if app closes mid change, this lets us store information without it being changed (we don't lose everything)
        try {
            cur = db.query(EXAM_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        Exams exam = new Exams();
                        exam.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));//check the method here, OrThrow might not be needed
                        exam.setTitle(cur.getString(cur.getColumnIndexOrThrow(TITLE)));
                        exam.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));
                        exam.setDate(cur.getString(cur.getColumnIndexOrThrow(DUE_DATE)));
                        exam.setLocation(cur.getString(cur.getColumnIndexOrThrow(LOCATION)));
                        exam.setClassName(cur.getString(cur.getColumnIndexOrThrow(CLASS_NAME)));
                        exam.setTime(cur.getString(cur.getColumnIndexOrThrow(TIME)));
                        examList.add(exam);
                    } while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return examList;
    }

    /**
     * Updates the status of a given exam based on its id
     * @param id the id of the exam to be updated
     * @param status the status that will be put into the exam
     */
    public void updateStatus(int id, int status) { //updates the status of the task (if it is complete or not)
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(EXAM_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    /**
     * Ypdates the exam comlpetely, with entirely new parameters
     * @param id the id of the exam to be updated
     * @param title the new title of the exam
     * @param date the new date of the exam
     * @param location the new location of the exam
     * @param className the new classNAme of the exam
     * @param time the new time of the exam
     */

    public void updateExam(int id, String title, String date, String location, String className, String time) { //Updates the name of the task
        ContentValues cv = new ContentValues();
        cv.put(TITLE, title);
        cv.put(DUE_DATE, date);
        cv.put(LOCATION, location);
        cv.put(CLASS_NAME, className);
        cv.put(TIME, time);
        db.update(EXAM_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    /**
     * Deletes the exam based on the id passed into the method
     * @param id the id of the exam to be deleted
     */
    public void deleteExam(int id) { //deletes the task
        db.delete(EXAM_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
    //^^^ in all above methods the Task item is found using the ID key.

}