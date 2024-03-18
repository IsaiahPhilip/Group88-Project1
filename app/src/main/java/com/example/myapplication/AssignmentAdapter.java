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

import com.example.myapplication.Model.Assignment;
import com.example.attemptatworkingapplication.R;
import com.example.myapplication.Utils.AssignmentDatabaseHandler;

import java.util.List;

/**
 * A public class that is meant to be used with the Assignment class, this class
 * adapts a list of assignments into a recycler view that is displayed in the app.
 */
public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentHolder>{
    private List<Assignment> assignmentList;
    private AssignmentView activity;
    private AssignmentDatabaseHandler db;

    /**
     * A constructor for the AssignmentAdapter Class
     * @param db the Database passed in
     * @param activity the activity view being used for the recycler view
     */
    public AssignmentAdapter(AssignmentDatabaseHandler db, AssignmentView activity) {
        this.db = db;
        this.activity = activity;
    }

    /**
     * A getter method for the AssignmentList variable
     * @return returns the assignment variable.
     */
    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    /**
     * A public method that deletes an assignment from the assignmentList, and the
     * database as well
     * @param position the position of the item to be deleted
     */
    public void deleteItem(int position) {
        Assignment item = assignmentList.get(position);
        db.deleteAssignment(item.getId());
        assignmentList.remove(position);
        notifyItemRemoved(position); //automatically updates recycler view when item is deleted
    }

    /**
     * A public method that creates assignments and adds them to the assignmentList,
     * also calls a special method in AssignmentDatabaseHelper to add it there
     * @param assignment the assignment to be added
     * @param position the position of the assignment to be added in the list
     * @param count a variable passed through to the db
     */

    public void createAssignment(Assignment assignment, int position, int count) {
        db.insertAssignmentSpecial(assignment, count);
        assignmentList.add(assignment);
        notifyItemInserted(position);
    }

    /**
     * on the view being created, this will run.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return returns the assignment holder with the item view
     */
    @NonNull
    public AssignmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_layout,
                parent, false);
        return new AssignmentHolder(itemView);
    }

    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public void onBindViewHolder(final AssignmentHolder holder, int position) {
        db.openDatabase();
        final Assignment assignment = assignmentList.get(position);
        String output = "Name: " + assignment.getTitle() + "\n" + "Due Date: " +
                assignment.getDueDate() + "\n" + "Class: " + assignment.getClassName();
        holder.assignment.setText(output);
        holder.assignment.setChecked(toBoolean( assignment.getStatus()));
        holder.assignment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus( assignment.getId(), 1); //if item is checked, updates status to 1 as complete
                    assignment.setStatus(1);
                } else {
                    db.updateStatus( assignment.getId(), 0);//if item is unchecked, updates status to 0 as incomplete
                    assignment.setStatus(0);
                }
            }

        });
    }

    /**
     * Gets the itemCount
     * @return the number of assignments
     */

    public int getItemCount() {
        return assignmentList.size();
    }

    /**
     * Converts an int to a boolean
     * @param n the int to be converted
     * @return returns the boolean true or false
     */

    private boolean toBoolean(int n) {
        return n!=0;
    }

    /**
     * A public method used to set the assignments in the list.
     * @param assignmentList the List of assignments to set.
     */

    public void setAssignments(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
        notifyDataSetChanged();
    }

    /**
     * Gets the context of the activity
     * @return returns the current activity
     */
    public Context getContext() {
        return activity;
    }

    /**
     * A public method used to edit items in the list.
     * @param position the position of the item.
     */
    public void editItem(int position) { //method that edits items
        Assignment item = assignmentList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("title", item.getTitle());
        bundle.putString("dueDate", item.getDueDate());
        bundle.putString("class", item.getClassName());
        AddNewAssignment fragment = new AddNewAssignment();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewAssignment.TAG);
    }

    /**
     * A public inner class that allows the assignmentHolder to work.
     */
    public static class AssignmentHolder extends RecyclerView.ViewHolder {
        CheckBox assignment;
        AssignmentHolder(View view) {
            super(view);
            assignment = view.findViewById(R.id.assignmentCheckBox);
        }
    }
}
