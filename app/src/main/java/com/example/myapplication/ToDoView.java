package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.attemptatworkingapplication.R;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The method that is the home screen for the todoList (first thing i tried to implement)
 */
public class ToDoView extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private ToDoAdapter customAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;
    private DatabaseHandler db;
    private Spinner taskSpinner;

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
        setContentView(R.layout.all_tasks);
        db = new DatabaseHandler(this);
        db.openDatabase();


        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, ToDoView.this);
        customAdapter = new ToDoAdapter(db, ToDoView.this);
        tasksRecyclerView.setAdapter(tasksAdapter);
        fab = findViewById(R.id.action_button);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ToDoRecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        List<ToDoModel> customList = new ArrayList<>();
        for (ToDoModel currAssign : taskList) {
            if (currAssign.getStatus() == 0) {
                customList.add(currAssign);
            }
        }
        customAdapter.setTasks(customList);

        taskSpinner = findViewById(R.id.task_sort_dropdown);
        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                if(position == 0) {
                    dateSorting(view);
                } else if(position == 1) {
                    checkedSorting(view);
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
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


    }
    /**
     * Dandles dialog closing by getting all todomodels from the db and setting this to
     * the todoModel adapter.
     * @param dialog the dialog that is closed
     */
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
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
     * A sort method that is based on whether or not the list items are checked
     * @param view the view passed in
     */
    public void checkedSorting(View view) {
        List<ToDoModel> taskInfo = tasksAdapter.getTasks();
        List<ToDoModel> tasksSaved = new ArrayList<ToDoModel>();
        int count = 0;
        for (ToDoModel task: taskInfo) {
            System.out.println(task.getStatus());
            ToDoModel newInfo = new ToDoModel();
            newInfo.setStatus(task.getStatus());
            newInfo.setTask(task.getTask());
            newInfo.setDueDate(task.getDueDate());
            tasksSaved.add(newInfo);
            count++;

        }
        Collections.sort(tasksSaved);

        for (int i = tasksAdapter.getTasks().size() - 1; i >= 0; i--) {
            tasksAdapter.deleteItem(i);
        }
        for (int i = (customAdapter.getTasks().size()) - 1; i >= 0; i--) {
            System.out.println("Deleting Items");
            customAdapter.deleteItem(i);
        }
        int i = 0;
        tasksAdapter.notifyDataSetChanged();
        List<ToDoModel> customList = new ArrayList<>();
        for (int x = 0; x < count; x++) {
            tasksAdapter.createTask(tasksSaved.get(x), i, i);
            if (tasksSaved.get(x).getStatus() == 0) {
                customList.add(tasksSaved.get(x));
            }
            i++;
        }
        customAdapter.setTasks(customList);
        taskList = tasksSaved;
        tasksRecyclerView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

    /**
     * A sort method that is based on the due date of the todolists
     * @param view the view passed in
     */
    public void dateSorting(View view) {
        List<ToDoModel> taskInfo = tasksAdapter.getTasks();
        List<ToDoModel> tasksSaved = new ArrayList<ToDoModel>();
        for (ToDoModel task: taskInfo) {
            System.out.println(task.getStatus());
            ToDoModel newInfo = new ToDoModel();
            newInfo.setStatus(task.getStatus());
            newInfo.setTask(task.getTask());
            newInfo.setDueDate(task.getDueDate());
            tasksSaved.add(newInfo);
        }
        for (int i = 0; i < tasksSaved.size(); i++) {

            for (int j = tasksSaved.size() - 1; j > i; j--) {
                if (tasksSaved.get(i).dateSort(tasksSaved.get(j)) > 0) {
                    ToDoModel tmp = tasksSaved.get(i);
                    tasksSaved.set(i,tasksSaved.get(j)) ;
                    tasksSaved.set(j,tmp);
                }

            }

        }
        for (int i = taskList.size() - 1; i >= 0; i--) {
            tasksAdapter.deleteItem(i);
        }
        int i = 0;
        tasksAdapter.notifyDataSetChanged();
        for (ToDoModel task: tasksSaved) {
            tasksAdapter.createTask(task, i, i);
            i++;
        }
        taskList = tasksSaved;
        tasksAdapter.notifyDataSetChanged();
        tasksRecyclerView.setAdapter(tasksAdapter);


    }



    //TODO
    //Make remaining buttons to add classes, Assignments, and Exams (homescreen will go to these)
    //Make a new page that pops up when adding a class, assignment, or exam (one exists, but not sufficient to add all information)
    //Make a separate page to view classes, Active assignments, and upcoming exams (pages that go from homescreen)
    //Make sort function for assignments based on complete variable, by due date, and course (objects must implement comparable to do this)
    //Make completed assignment disappear from list of active assignments DONE!!!! YAY!!!
    //exams should have details listed under it
    //make to do lists (Separate class? Linked List? Priority Queue?) Partially done, not completely implemented

}