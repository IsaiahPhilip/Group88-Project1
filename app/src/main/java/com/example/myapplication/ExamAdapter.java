package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Exams;
import com.example.attemptatworkingapplication.R;
import com.example.myapplication.Utils.ExamHandler;

import java.util.List;

/**
 * A public class that is meant to be used with the Exam class, this class
 * adapts a list of exams into a recycler view that is displayed in the app.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.examViewHolder> {
    private List<Exams> examList;
    private ExamView activity;
    private ExamHandler db;

    /**
     * Constructor for examAdapter
     * @param db the database being added
     * @param activity the activity the recycler view will be displayed in
     */
    public ExamAdapter(ExamHandler db, ExamView activity) {
        this.db = db;
        this.activity = activity;
    }

    /**
     * a public class that deletes an exam from a target position
     * @param position the position of the exam to be deleted.
     */
    public void deleteExam(int position) {
        Exams exam = examList.get(position);
        db.deleteExam(exam.getId());
        examList.remove(position);
        notifyItemRemoved(position); //automatically updates recycler view when item is deleted
    }
    /**
     * on the view being created, this will run.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return returns the exam holder with the item view
     */
    @NonNull
    public examViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_layout,
                parent, false);
        return new examViewHolder(itemView);
    }

    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public void onBindViewHolder(final examViewHolder holder, int position) {
        db.openDatabase();
        final Exams exam = examList.get(position);
        String output = "Name: " + exam.getTitle() + "\n" + "Date: " + exam.getDate() + "\n" +
                "Time: " + exam.getTime() + "\n" + "Class: " + exam.getClassName()  + "\n" +
                "Location: " + exam.getLocation();
        holder.exam.setText(output);
        holder.exam.setChecked(toBoolean(exam.getStatus()));
        holder.exam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(exam.getId(), 1); //if item is checked, updates status to 1 as complete
                    exam.setStatus(1);
                } else {
                    db.updateStatus(exam.getId(), 0);//if item is unchecked, updates status to 0 as incomplete
                    exam.setStatus(0);
                }
            }

        });
    }

    /**
     * Gets the number of items in the examList
     * @return
     */
    public int getItemCount() {
        return examList.size();
    }

    /**
     * Converts an int into a boolean
     * @param n the int to be converted
     * @return true or false depending on the value of n
     */
    private boolean toBoolean(int n) {
        return n!=0;
    }

    /**
     * Sets the exam list to a passed in examList
     * @param examList the exam list to set the examList variable to.
     */
    public void setExams(List<Exams> examList) {
        this.examList = examList;
        notifyDataSetChanged();
    }

    /**
     * Gets the context of the adapter
     * @return returns the activity being run in.
     */
    public Context getContext() {
        return activity;
    }
    /**
     * A public method used to edit items in the list.
     * @param position the position of the item.
     */
    public void editExam(int position) { //method that edits items
        Exams exam = examList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", exam.getId());
        bundle.putString("title", exam.getTitle());
        bundle.putString("className", exam.getClassName());
        bundle.putString("dueDate", exam.getDate());
        bundle.putString("time", exam.getTime());
        bundle.putString("location", exam.getLocation());
        AddNewExam fragment = new AddNewExam();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewExam.TAG);
    }
    /**
     * A public inner class that allows the assignmentHolder to work.
     */



    public static class examViewHolder extends RecyclerView.ViewHolder {
        CheckBox exam;
        examViewHolder(View view) {
            super(view);
            exam = view.findViewById(R.id.examCheckBox);
        }
    }
}