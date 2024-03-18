package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Model.CollegeClass;

import java.util.ArrayList;
import java.util.List;

public class CollegeDatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "collegeClassDatabase";
    private static final String CLASSES_TABLE = "Classes";
    private static final String ID = "id";
    private static final String PROFESSOR = "professor";
    private static final String CLASS_SECTION = "ClassSection";
    private static final String BUILDING = "Building";
    private static final String ROOMNUMBER = "RoomNumber";
    private static final String TIME = "time";
    private static final String DAYS = "days";
    private static final String CLASSNAME = "ClassName";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + CLASSES_TABLE + "(" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + CLASSNAME + " TEXT, " + PROFESSOR + " TEXT, " +
            CLASS_SECTION + " TEXT, " + BUILDING + " TEXT, " + ROOMNUMBER + " TEXT, " + TIME +
            " TEXT, " + DAYS + " TEXT)";
    private SQLiteDatabase db;
    public CollegeDatabaseHandler(Context context) {
        super(context, NAME, null, VERSION); //constructer for the method, calls the super constructor
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE); //when the class is created, it creates an SQLite table based on parameters listed in the string CREATE_TODO_TABLE
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CLASSES_TABLE); //drops table if a new table needs to be created
        onCreate(db); //creates table again
    }
    public void openDatabase() {
        db = this.getWritableDatabase(); //opens the writable SQLite database
    }
    /**
     * Inserts a new assignment into the database
     * @param newClass the new class to be added
     */
    public void insertClass(CollegeClass newClass) {
        ContentValues cv = new ContentValues();
        cv.put(CLASSNAME, newClass.getName()); //Adds a new task to the SQLite database with task as string input
        cv.put(PROFESSOR, newClass.getProfessor()); //Adds a new task to the SQLite database with Status 0 (uncomplete)
        cv.put(CLASS_SECTION, newClass.getClassSection());
        cv.put(BUILDING, newClass.getBuilding());
        cv.put(ROOMNUMBER, newClass.getRoomNumber());
        cv.put(TIME, newClass.getTime());
        cv.put(DAYS, newClass.getClassDates());


        db.insert(CLASSES_TABLE, null, cv); //inserts data we just created into the TODO_TABLE
    }
    /**
     * Gets all the classes stored in the list.
     * @return
     */

    public List<CollegeClass> getAllClasses() { //This method is pretty easy, just gets a list of all tasks to return
        List<CollegeClass> classList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction(); //if app closes mid change, this lets us store information without it being changed (we don't lose everything)
        try {
            cur = db.query(CLASSES_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        CollegeClass currClass = new CollegeClass();
                        currClass.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));//check the method here, OrThrow might not be needed
                        currClass.setName(cur.getString(cur.getColumnIndexOrThrow(CLASSNAME)));
                        currClass.setProfessor(cur.getString(cur.getColumnIndexOrThrow(PROFESSOR)));
                        currClass.setClassSection(cur.getString(cur.getColumnIndexOrThrow(CLASS_SECTION)));
                        currClass.setBuilding(cur.getString(cur.getColumnIndexOrThrow(BUILDING)));
                        currClass.setRoomNumber(cur.getString(cur.getColumnIndexOrThrow(ROOMNUMBER)));
                        currClass.setTime(cur.getString(cur.getColumnIndexOrThrow(TIME)));
                        currClass.setClassDates(cur.getString(cur.getColumnIndexOrThrow(DAYS)));
                        currClass.setProfessor(cur.getString(cur.getColumnIndexOrThrow(PROFESSOR)));
                        classList.add(currClass);
                    } while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return classList;
    }

    /**
     * Method that updates a class already in the db
     * @param id id of the assignment
     * @param className classNAme of the assignment
     * @param professor professor of the class
     * @param classSection section of the class
     * @param building building the class is in
     * @param roomNumber the room number of the class
     * @param time times the class takes place
     * @param days the days the class repeats on
     */
    public void updateClass(int id, String className, String professor, String classSection, String building,
                            String roomNumber, String time, String days) { //Updates the name of the task
        ContentValues cv = new ContentValues();
        cv.put(CLASSNAME, className);
        cv.put(PROFESSOR, professor);
        cv.put(CLASS_SECTION, classSection);
        cv.put(BUILDING, building);
        cv.put(ROOMNUMBER, roomNumber);
        cv.put(TIME, time);
        cv.put(DAYS, days);
        db.update(CLASSES_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    /**
     * Deletes a class based on a passed in id
     * @param id the id of the class to be deleted
     */
    public void deleteClass(int id) { //deletes the task
        db.delete(CLASSES_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

}
