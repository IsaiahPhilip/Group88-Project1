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

import com.example.myapplication.Model.ToDoModel;
import com.example.attemptatworkingapplication.R;
import com.example.myapplication.Utils.DatabaseHandler;

import java.util.List;

/**
 * Class that is very complex and is a bit easy to get lost in, that said...
 * Most methods are self explanatory, good video to watch if not understanding is here:
 * https://youtu.be/27MXFSWC1_4?si=uac1ykh9arff3BR4
 * Essentially the method sets up the recycler view used to add and delete tasks
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private ToDoView activity;
    private DatabaseHandler db;

    /**
     * a public constructor that builds a ToDoAdapter
     * @param db
     * @param activity
     */
    public ToDoAdapter(DatabaseHandler db, ToDoView activity) {
        this.db = db;
        this.activity = activity;
    }

    /**
     * A public method that deletes the item at a target position
     * @param position the position to be deleted
     */
    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position); //automatically updates recycler view when item is deleted

    }

    /**
     * creates a new task to be added to the list
     * @param task the task variable to be added
     * @param position the position to be added at
     * @param count a count bassed through to the database
     */
    public void createTask(ToDoModel task, int position, int count) {
        db.insertTaskSpecial(task, count);
        todoList.add(task);
        notifyItemInserted(position);
        System.out.println("Task added");
    }
    /**
     * on the view being created, this will run.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return returns the View holder with the item view
     */

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,
                parent, false);
        return new ViewHolder(itemView);
    }

    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public void onBindViewHolder(final ViewHolder holder, int position) {
        db.openDatabase();
        final ToDoModel item = todoList.get(position);
        String output = "Task: " + item.getTask() + "\n" + "Due date: " + item.getDueDate();
        holder.task.setText(output);
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1); //if item is checked, updates status to 1 as complete
                    item.setStatus(1);
                } else {
                    db.updateStatus(item.getId(), 0);//if item is unchecked, updates status to 0 as incomplete
                    item.setStatus(0);
                }
            }

        });
    }

    /**
     * Gets the number of items in the todoList
     * @return the number of items in the todoList
     */

    public int getItemCount() {
        return todoList.size();
    }

    /**
     * Converts an int to a boolean
     * @param n the int to be converted
     * @return true or false depending on the int passed in
     */

    private boolean toBoolean(int n) {
        return n!=0;
    }

    /**
     * Sets the todoList equal to a passed in todoList
     * @param todoList the list to set the todoList equal to.
     */

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    /**
     * Gets the tasks in the todolist
     * @return returns the todoList
     */
    public List<ToDoModel> getTasks() {
        return todoList;
    }

    /**
     * gets the context of the class, returning the activity it runs in
     * @return returns the activity ToDoAdapter is running in.
     */
    public Context getContext() {
        return activity;
    }
    /**
     * A public method used to edit items in the list.
     * @param position the position of the item.
     */
    public void editItem(int position) { //method that edits items
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("dueDate", item.getDueDate());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    /**
     * A public inner class that allows the assignmentHolder to work.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}