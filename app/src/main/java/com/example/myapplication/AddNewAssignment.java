package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.attemptatworkingapplication.R;
import com.example.myapplication.Model.Assignment;
import com.example.myapplication.Utils.AssignmentDatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

/**
 * Method to add a new assignment, nothing too crazy here. This method is the one that
 * causes the tab to open where we put in the information
 */
public class AddNewAssignment extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newAssignmentText;
    private EditText newAssignmentDateDue;
    private EditText newAssignmentAssociatedClass;
    private Button newAssignmentSaveButton;
    private AssignmentDatabaseHandler db;
    public static AddNewAssignment newInstance() {
        return new AddNewAssignment();
    }

    /**
     * Runs when this is created
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);

    }

    /**
     * Runs when this creates a view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the view that is inflated
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_assignment, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //Check the above line (personal note)^^^
        return view;
    }

    /**
     * Runs when the view is created
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newAssignmentText = requireView().findViewById(R.id.newAssignmentTitle);
        newAssignmentDateDue = requireView().findViewById(R.id.newAssignmentDueDate);
        newAssignmentAssociatedClass = requireView().findViewById(R.id.newAssignmentClass);
        newAssignmentSaveButton = requireView().findViewById(R.id.newAssignmentButtonCreate);

        db = new AssignmentDatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String title = bundle.getString("title");
            String dueDate = bundle.getString("dueDate");
            String associatedClass = bundle.getString("class");
            newAssignmentText.setText(title);
            newAssignmentDateDue.setText(dueDate);
            newAssignmentAssociatedClass.setText(associatedClass);
            assert title != null;
            assert dueDate != null;
            assert associatedClass != null;
            if (associatedClass.length() > 0) {
                newAssignmentSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
            }
        }
        newAssignmentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newAssignmentSaveButton.setEnabled(false);
                    newAssignmentSaveButton.setTextColor(Color.GRAY);
                } else {
                    newAssignmentSaveButton.setEnabled(true);
                    newAssignmentSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        newAssignmentSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = newAssignmentText.getText().toString();
                String dueDate = newAssignmentDateDue.getText().toString();
                String associatedClass = newAssignmentAssociatedClass.getText().toString();
                if (finalIsUpdate) {
                    db.updateAssignment(bundle.getInt("id"), title, dueDate, associatedClass);
                } else {
                    Assignment assignment = new Assignment();
                    assignment.setTitle(title);
                    assignment.setDueDate(dueDate);
                    assignment.setClassName(associatedClass);
                    db.insertAssignment(assignment);
                }
                dismiss();
            }
        });
    }

    /**
     * Runs when the dialog gets dismissed
     * @param dialog the dialog that was dismissed will be passed into the
     *               method
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }
}
