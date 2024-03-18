package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.CollegeClass;
import com.example.attemptatworkingapplication.R;
import com.example.myapplication.Utils.CollegeDatabaseHandler;

import java.util.List;

public class CollegeClassAdapter extends RecyclerView.Adapter<CollegeClassAdapter.classViewHolder> {
    private List<CollegeClass> classList;
    private CollegeClassesView activity;
    private CollegeDatabaseHandler db;
    public CollegeClassAdapter(CollegeDatabaseHandler db, CollegeClassesView activity) {
        this.db = db;
        this.activity = activity;
    }
    public void deleteClassItem(int position) {
        CollegeClass item = classList.get(position);
        db.deleteClass(item.getId());
        classList.remove(position);
        notifyItemRemoved(position); //automatically updates recycler view when item is deleted
    }
    /**
     * on the view being created, this will run.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return returns the class holder with the item view
     */
    @NonNull
    public classViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_layout,
                parent, false);
        return new classViewHolder(itemView);
    }

    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public void onBindViewHolder(final classViewHolder holder, int position) {
        db.openDatabase();
        final CollegeClass collegeClass = classList.get(position);
        String output = "Name: " + collegeClass.getName() + "\n" + "Professor: " + collegeClass.getProfessor() +
                "\n" + "Section: " + collegeClass.getClassSection() + "\n" + "Days: " + collegeClass.getClassDates() +
                "\n" + "Location: " + collegeClass.getBuilding() + ", " + collegeClass.getRoomNumber() + "\n" + "Time: " +
                collegeClass.getTime();
        holder.yourClass.setText(output);
    }

    /**
     * gets the count of the number of items in the class list
     * @return the number of items in the class list
     */
    public int getItemCount() {
        return classList.size();
    }

    /**
     * Sets the classes to a variable passed in
     * @param classList the variable to set the classlist to
     */
    public void setClasses(List<CollegeClass> classList) {
        this.classList = classList;
        notifyDataSetChanged();
    }

    /**
     * Gets the context and returns the activity of the file this runs in
     * @return
     */
    public Context getContext() {
        return activity;
    }
    /**
     * A public method used to edit items in the list.
     * @param position the position of the item.
     */
    public void editClassItem(int position) { //method that edits items
        CollegeClass item = classList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("Class name", item.getName());
        bundle.putString("professor", item.getProfessor());
        bundle.putString("Class Section", item.getClassSection());
        bundle.putString("Building", item.getBuilding());
        bundle.putString("RoomNumber", item.getRoomNumber());
        bundle.putString("time", item.getTime());
        bundle.putString("days", item.getClassDates());
        AddNewClass fragment = new AddNewClass();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewClass.TAG);
    }
    /**
     * A public inner class that allows the assignmentHolder to work.
     */
    public static class classViewHolder extends RecyclerView.ViewHolder {
        TextView yourClass;

        classViewHolder(View view) {
            super(view);
            yourClass = view.findViewById(R.id.classesText);
        }
    }
}
