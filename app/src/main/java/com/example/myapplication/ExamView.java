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
import com.example.myapplication.ExamAdapter;
import com.example.myapplication.Model.Exams;
import com.example.myapplication.Utils.ExamHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

/**
 * View for the exams that are shown in the app
 */
public class ExamView extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView examRecyclerView;
    private ExamAdapter examAdapter;
    private FloatingActionButton fab;
    private List<Exams> examList;
    private ExamHandler db;

    /**
     * Activates on create of the activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_exams);
        db = new ExamHandler(this);
        db.openDatabase();


        examRecyclerView = findViewById(R.id.examRecyclerView);
        examRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        examAdapter = new ExamAdapter(db, ExamView.this);
        examRecyclerView.setAdapter(examAdapter);
        fab = findViewById(R.id.action_button_exam);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ExamRecyclerItemTouchHelper(examAdapter));
        itemTouchHelper.attachToRecyclerView(examRecyclerView);

        examList = db.getAllExams();
        Collections.reverse(examList);
        examAdapter.setExams(examList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewExam.newInstance().show(getSupportFragmentManager(), AddNewExam.TAG);
            }
        });


    }
    /**
     * Dandles dialog closing by getting all exams from the db and setting this to
     * the exam adapter.
     * @param dialog the dialog that is closed
     */
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        examList = db.getAllExams();
        Collections.reverse(examList);
        examAdapter.setExams(examList);
        examAdapter.notifyDataSetChanged();
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
