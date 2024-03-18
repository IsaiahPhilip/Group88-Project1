package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attemptatworkingapplication.R;

/**
 * Main screen of the app, essentially just contains methods that go to other places
 */
public class HomeScreen extends AppCompatActivity {
    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    /**
     * Goes to the todolist activity
     * @param view the view passed in
     */
    public void goToTodoList(View view) {
        Intent Intent = new Intent(this, ToDoView.class);
        startActivity(Intent);
    }

    /**
     * Goes to the CollegeClass activity
     * @param view the view passed in
     */
    public void goToClassList(View view) {
        Intent Intent = new Intent(this, CollegeClassesView.class);
        startActivity(Intent);
    }

    /**
     * Goes to the exam Activity
     * @param view the view passed in
     */
    public void goToExamList(View view) {
        Intent Intent = new Intent(this, ExamView.class);
        startActivity(Intent);
    }

    /**
     * goes to the assignment activity
     * @param view the view passed in
     */
    public void goToAssignmentList(View view) {
        Intent Intent = new Intent(this, AssignmentView.class);
        startActivity(Intent);
    }

}
