package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attemptatworkingapplication.R;
import com.example.myapplication.AssignmentAdapter;
import com.example.myapplication.Model.Assignment;
import com.example.myapplication.Utils.AssignmentDatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The view that holds the assignments
 */
public class AssignmentView extends AppCompatActivity implements DialogCloseListener{
    private RecyclerView assignmentRecyclerView;
    private AssignmentAdapter assignmentAdapter;
    private AssignmentAdapter customAdapter;
    private FloatingActionButton fab;
    private List<Assignment> assignmentList;
    private AssignmentDatabaseHandler db;
    private Spinner assignmentSpinner;

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
        setContentView(R.layout.all_assignments);
        db = new AssignmentDatabaseHandler(this);
        db.openDatabase();


        assignmentRecyclerView = findViewById(R.id.assignmentsRecyclerView);
        assignmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assignmentAdapter = new AssignmentAdapter(db, AssignmentView.this);
        customAdapter = new AssignmentAdapter(db, AssignmentView.this);
        assignmentRecyclerView.setAdapter(assignmentAdapter);
        fab = findViewById(R.id.assignment_button);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new AssignmentRecycleTouchHelper(assignmentAdapter));
        itemTouchHelper.attachToRecyclerView(assignmentRecyclerView);

        assignmentList = db.getAllAssignments();
        Collections.reverse(assignmentList);
        assignmentAdapter.setAssignments(assignmentList);
        List<Assignment> customList = new ArrayList<>();
        for (Assignment currAssign : assignmentList) {
            if (currAssign.getStatus() == 0) {
                customList.add(currAssign);
            }
        }
        customAdapter.setAssignments(customList);

        assignmentSpinner = findViewById(R.id.assignment_sort_dropdown);
        assignmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                if(position == 0) {
                    dateSortingAssign(view);
                } else if(position == 1) {
                    checkedSortingAssign(view);
                } else {
                    courseSortingAssign(view);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewAssignment.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


    }

    /**
     * Dandles dialog closing by getting all assignments from the db and setting this to
     * the assignment adapter.
     * @param dialog the dialog that is closed
     */
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        assignmentList = db.getAllAssignments();
        Collections.reverse(assignmentList);
        assignmentAdapter.setAssignments(assignmentList);
        assignmentAdapter.notifyDataSetChanged();
    }

    /**
     * Method that returns user to the home screen when that button is pressed.
     * @param view
     */
    public void goHome(View view) {
        Intent Intent = new Intent(this, HomeScreen.class);
        startActivity(Intent);
    }

    /**
     * Method that sorts the Assignments based on whether or not they are checked (complete)
     * @param view the view being checked
     */
    public void checkedSortingAssign(View view) {
        List<Assignment> assignInfo = assignmentAdapter.getAssignmentList();
        List<Assignment> assignSaved = new ArrayList<Assignment>();
        int count = 0;
        for (Assignment assignment: assignInfo) {
            Assignment newInfo = new Assignment();
            newInfo.setStatus(assignment.getStatus());
            newInfo.setClassName(assignment.getClassName());
            newInfo.setDueDate(assignment.getDueDate());
            newInfo.setTitle(assignment.getTitle());
            assignSaved.add(newInfo);
            count++;
        }
        Collections.sort(assignSaved);
        System.out.println("I'm here");
        System.out.println(assignmentAdapter.getAssignmentList().size() - 1);
        System.out.println(assignmentList.size() - 1);

        for (int i = (assignmentAdapter.getAssignmentList().size()) - 1; i >= 0; i--) {
            System.out.println("Deleting Items");
            assignmentAdapter.deleteItem(i);
        }
        for (int i = (customAdapter.getAssignmentList().size()) - 1; i >= 0; i--) {
            System.out.println("Deleting Items");
            customAdapter.deleteItem(i);
        }
        int i = 0;
        assignmentAdapter.notifyDataSetChanged();
        List<Assignment> customList = new ArrayList<>();
        for (int x = 0; x < count; x++) {
            assignmentAdapter.createAssignment(assignSaved.get(x), i, i);
            if (assignSaved.get(x).getStatus() == 0) {
                customList.add(assignSaved.get(x));
            }
            i++;
        }
        customAdapter.setAssignments(customList);
        assignmentList = assignSaved;
        assignmentRecyclerView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

    /**
     * Method that sorts the assignments based on the dueDate
     * @param view the view they are being sorted in
     */
    public void dateSortingAssign(View view) {
        List<Assignment> assignInfo = assignmentAdapter.getAssignmentList();
        List<Assignment> assignSaved = new ArrayList<Assignment>();
        for (Assignment assignment: assignInfo) {
            Assignment newInfo = new Assignment();
            newInfo.setStatus(assignment.getStatus());
            newInfo.setClassName(assignment.getClassName());
            newInfo.setDueDate(assignment.getDueDate());
            newInfo.setTitle(assignment.getTitle());
            assignSaved.add(newInfo);
        }
        for (int i = 0; i < assignSaved.size(); i++) {

            for (int j = assignSaved.size() - 1; j > i; j--) {
                if (assignSaved.get(i).dateSortAssign(assignSaved.get(j)) > 0) {
                    Assignment tmp = assignSaved.get(i);
                    assignSaved.set(i,assignSaved.get(j)) ;
                    assignSaved.set(j,tmp);
                }

            }

        }
        for (int i = assignmentList.size() - 1; i >= 0; i--) {
            assignmentAdapter.deleteItem(i);
        }
        int i = 0;
        assignmentAdapter.notifyDataSetChanged();
        for (Assignment assignment: assignSaved) {
            assignmentAdapter.createAssignment(assignment, i, i);
            i++;
        }
        assignmentList = assignSaved;
        assignmentRecyclerView.setAdapter(assignmentAdapter);
        assignmentAdapter.notifyDataSetChanged();

    }

    /**
     * Method that sorts the assignments based on the className
     * @param view the view they are being sorted in
     */
    public void courseSortingAssign(View view) {
        List<Assignment> assignInfo = assignmentAdapter.getAssignmentList();
        List<Assignment> assignSaved = new ArrayList<Assignment>();
        for (Assignment assignment: assignInfo) {
            Assignment newInfo = new Assignment();
            newInfo.setStatus(assignment.getStatus());
            newInfo.setClassName(assignment.getClassName());
            newInfo.setDueDate(assignment.getDueDate());
            newInfo.setTitle(assignment.getTitle());
            assignSaved.add(newInfo);
        }
        for (int i = 0; i < assignSaved.size(); i++) {

            for (int j = assignSaved.size() - 1; j > i; j--) {
                if (assignSaved.get(i).courseSort(assignSaved.get(j)) > 0) {
                    Assignment tmp = assignSaved.get(i);
                    assignSaved.set(i,assignSaved.get(j)) ;
                    assignSaved.set(j,tmp);
                }
            }
        }
        for (int i = assignmentList.size() - 1; i >= 0; i--) {
            assignmentAdapter.deleteItem(i);
        }
        int i = 0;
        assignmentAdapter.notifyDataSetChanged();
        for (Assignment assignment: assignSaved) {
            assignmentAdapter.createAssignment(assignment, i, i);
            i++;
        }
        assignmentList = assignSaved;
        assignmentRecyclerView.setAdapter(assignmentAdapter);
        assignmentAdapter.notifyDataSetChanged();

    }
}
