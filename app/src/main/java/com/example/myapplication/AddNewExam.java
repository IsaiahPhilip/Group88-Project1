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
import com.example.myapplication.Model.Exams;
import com.example.myapplication.Utils.ExamHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewExam extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText examTitle;
    private EditText examRelatedClass;
    private EditText examDate;
    private EditText examTime;
    private EditText examLocation;
    private Button newExamSaveButton;
    private ExamHandler db;
    public static AddNewExam newInstance() {
        return new AddNewExam();
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
        View view = inflater.inflate(R.layout.new_exam, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        examTitle = requireView().findViewById(R.id.newExamTitle);
        examRelatedClass = requireView().findViewById(R.id.newExamRelatedClass);
        examDate = requireView().findViewById(R.id.newExamDate);
        examTime = requireView().findViewById(R.id.newExamTime);
        examLocation = requireView().findViewById(R.id.newExamLocation);
        newExamSaveButton = requireView().findViewById(R.id.newExamButtonCreate);

        db = new ExamHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String newexamTitle = bundle.getString("title");
            String newexamRelatedClass = bundle.getString("className");
            String newexamDate = bundle.getString("dueDate");
            String newexamTime = bundle.getString("time");
            String newexamLocation = bundle.getString("location");
            examTitle.setText(newexamTitle);
            examRelatedClass.setText(newexamRelatedClass);
            examDate.setText(newexamDate);
            examTime.setText(newexamTime);
            examLocation.setText(newexamLocation);


            assert newexamTitle != null;
            assert newexamRelatedClass != null;
            assert newexamDate != null;
            assert newexamTime != null;
            assert newexamLocation != null;
            if (newexamLocation.length() > 0) {
                newExamSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
            }
        }
        examLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newExamSaveButton.setEnabled(false);
                    newExamSaveButton.setTextColor(Color.GRAY);
                } else {
                    newExamSaveButton.setEnabled(true);
                    newExamSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        newExamSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = examTitle.getText().toString();
                String relatedClass = examRelatedClass.getText().toString();
                String date = examDate.getText().toString();
                String time = examTime.getText().toString();
                String location = examLocation.getText().toString();
                if (finalIsUpdate) {
                    db.updateExam(bundle.getInt("id"), title, relatedClass, date, time, location);
                } else {
                    Exams exam = new Exams();
                    exam.setTitle(title);
                    exam.setLocation(location);
                    exam.setClassName(relatedClass);
                    exam.setTime(time);
                    exam.setDate(date);
                    db.insertExam(exam);
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
