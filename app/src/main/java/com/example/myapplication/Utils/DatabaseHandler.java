package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The DatabaseHandler class is the database for the ToDoModel and todoList section of
 * the app. Complicated class so I've annotated decently, ask me about any questions.
 */


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DUE_DATE = "dueDate";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK  + " TEXT, " + DUE_DATE + " TEXT, " +
            STATUS + " INTEGER)";
    //^^^^^^^^This string above is the initiation of the SQLite table. Essentially the purple purple
    //comments are the names of the columns (except TODO_TABLE, this is the name of the table)
    //Make sure these are EXACT before emulating or you will run into problems that I can explain
    private SQLiteDatabase db;

    /**
     * A constructor the builds a new database given the context passed in
     * @param context the context for the DatabaseHAndler
     */

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION); //constructer for the method, calls the super constructor
    }

    /**
     * Runs when the db is created, executes the SQL to make a database
     * @param db The database.
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE); //when the class is created, it creates an SQLite table based on parameters listed in the string CREATE_TODO_TABLE
    }

    /**
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE); //drops table if a new table needs to be created
        onCreate(db); //creates table again
    } //^^^This method DOES NOT work on startup, errors from column names and such need to be fixed in the cache of the emulator
    public void openDatabase() {
        db = this.getWritableDatabase(); //opens the writable SQLite database
    }

    /**
     * public method that inserts a Task into the database
     * @param task the task to be inserted
     */
    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask()); //Adds a new task to the SQLite database with task as string input
        cv.put(STATUS, task.getStatus()); //Adds a new task to the SQLite database with Status 0 (uncomplete)
        cv.put(DUE_DATE, task.getDueDate());

        db.insert(TODO_TABLE, null, cv); //inserts data we just created into the TODO_TABLE
    }

    /**
     * A special version of the insertTask method that takes uses another parameter
     * to set a custom ID meant for sorting.
     * @param task The task to be inserted
     * @param newID the ID to be given to the new task
     */
    public void insertTaskSpecial(ToDoModel task, int newID) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask()); //Adds a new task to the SQLite database with task as string input
        cv.put(STATUS, task.getStatus()); //Adds a new task to the SQLite database with Status 0 (uncomplete)
        cv.put(DUE_DATE, task.getDueDate());
        cv.put(ID, newID);

        db.insert(TODO_TABLE, null, cv); //inserts data we just created into the TODO_TABLE
    }

    /**
     * Method that gets all tasks from the database
     * @return a list of all tasks in the database
     */

    public List<ToDoModel> getAllTasks() { //This method is pretty easy, just gets a list of all tasks to return
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction(); //if app closes mid change, this lets us store information without it being changed (we don't lose everything)
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();

                        task.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));//check the method here, OrThrow might not be needed
                        task.setTask(cur.getString(cur.getColumnIndexOrThrow(TASK)));
                        task.setDueDate(cur.getString(cur.getColumnIndexOrThrow(DUE_DATE)));
                        task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));

                        taskList.add(task);
                    } while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    /**
     * Method that updates the task name of a given item
     * @param id the id to be updated
     * @param task the name of the task
     * @param dueDate date the task is due
     */

    public void updateTask(int id, String task, String dueDate) { //Updates the name of the task
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(DUE_DATE, dueDate);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    /**
     * Method deletes the task with the given id
     * @param id the id of the task to be deleted
     */
    public void deleteTask(int id) { //deletes the task
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

    /**
     * Method that updates the status of a given task
     * @param id the id of the status to update
     * @param status the status the item should be updated to
     */
    public void updateStatus(int id, int status) { //updates the status of the task (if it is complete or not)
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }


    //^^^ in all above methods the Task item is found using the ID key.

}
