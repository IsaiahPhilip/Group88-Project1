package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attemptatworkingapplication.R;
import com.example.myapplication.CollegeClassAdapter;
import com.example.myapplication.Model.CollegeClass;
import com.example.myapplication.Utils.CollegeDatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

/**
 * View for the college classes in the app.
 */
public class CollegeClassesView extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView collegeClassesRecyclerView;
    private CollegeClassAdapter classAdapter;
    private FloatingActionButton fab;
    private List<CollegeClass> classList;
    private CollegeDatabaseHandler db;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.college_classes);
        db = new CollegeDatabaseHandler(this);
        db.openDatabase();


        collegeClassesRecyclerView = findViewById(R.id.classesRecyclerView);
        collegeClassesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classAdapter = new CollegeClassAdapter(db, CollegeClassesView.this);
        collegeClassesRecyclerView.setAdapter(classAdapter);
        fab = findViewById(R.id.action_button_create_classes);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CollegeRecyclerItemTouchHelper(classAdapter));
        itemTouchHelper.attachToRecyclerView(collegeClassesRecyclerView);

        classList = db.getAllClasses();
        Collections.reverse(classList);
        classAdapter.setClasses(classList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewClass.newInstance().show(getSupportFragmentManager(), AddNewClass.TAG);
            }
        });


    }
    /**
     * Dandles dialog closing by getting all classes from the db and setting this to
     * the class adapter.
     * @param dialog the dialog that is closed
     */
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        classList = db.getAllClasses();
        Collections.reverse(classList);
        classAdapter.setClasses(classList);
        classAdapter.notifyDataSetChanged();
    }
    /**
     * Method that returns user to the home screen when that button is pressed.
     * @param view
     */
    public void goHome(View view) {
        Intent Intent = new Intent(this, HomeScreen.class);
        startActivity(Intent);
    }


}
