package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attemptatworkingapplication.R;
import com.example.myapplication.ExamAdapter;

/**
 * Class that allows exam items to be swiped through
 */
public class ExamRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ExamAdapter adapter;

    /**
     * Constructor for examAdapter
     * @param adapter the adapter to be set
     */

    public ExamRecyclerItemTouchHelper(ExamAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); //allows for left and right swiping
        this.adapter = adapter;
    }

    /**
     * Class isn't used, just needs to be defined
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to.
     * @param viewHolder   The ViewHolder which is being dragged by the user.
     * @param target       The ViewHolder over which the currently active item is being
     *                     dragged.
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false; //class isn't being used at the moment, just defined.
    }

    /**
     *
     * @param viewHolder The ViewHolder which has been swiped by the user.
     * @param direction  The direction to which the ViewHolder is swiped.
     */
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition(); //gets position of item we are working on
        if (direction == ItemTouchHelper.RIGHT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext()); //creates an alert befrore deleting
            builder.setTitle("Delete Exam"); //sets title of alert
            builder.setMessage("Do you want to delete this Exam?"); //sets message of alert
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { //creates a button for confirming deletion
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteExam(position); //if button is clicked, task will be deleted.
                }
            });
            builder.setNegativeButton("no", new DialogInterface.OnClickListener() { //creates a button to not delete item
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition()); //notifies item changed in position
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            adapter.editExam(position); //calls the edit method already defined.
        }
    }

    /**
     *
     * @param c                 The canvas which RecyclerView is drawing its children
     * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder        The ViewHolder which is being interacted by the User or it was
     *                          interacted and simply animating to its original position
     * @param dx                The amount of horizontal displacement caused by user's action
     * @param dy                The amount of vertical displacement caused by user's action
     * @param actionState       The type of interaction on the View.
     * @param isActive True if this view is currently being controlled by the user or
     *                          false it is simply animating back to its original state.
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dx, float dy, int actionState, boolean isActive) { //dx is change in x, dy is change in y
        super.onChildDraw(c, recyclerView, viewHolder, dx, dy, actionState, isActive);

        Drawable icon;
        ColorDrawable background; //changes color of the background when swiping.
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; //TODO experiment with this to see best values

        if (dx < 0) { //dx is > 0 when swiping to the right direction, and < when left
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark)); //sets background color when swiping to edit
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.baseline_delete_forever);
            background = new ColorDrawable(Color.RED); //sets icon color when deleting
        }
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2; //so we don't exceed icon views
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dx > 0) { //swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom); //takes the variables just created and set bounds according to height and width

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)dx)
                    + backgroundCornerOffset, itemView.getBottom());

        } else if (dx < 0) { //swiping ot the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom); //takes the variables just created and set bounds according to height and width

            background.setBounds(itemView.getRight() + ((int) dx) - backgroundCornerOffset, itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());


        } else {
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);

    }
}
