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
import com.example.myapplication.Model.CollegeClass;
import com.example.myapplication.Utils.CollegeDatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;
/**
 * Method to add a new class, nothing too crazy here. This method is the one that
 * causes the tab to open where we put in the information
 */

public class AddNewClass extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newClassName;
    private EditText newClassProfessor;
    private EditText newClassSection;
    private EditText newClassBuilding;
    private EditText newClassRoomNumber;
    private EditText newClassTime;
    private EditText newClassDates;

    private Button newClassSaveButton;
    private CollegeDatabaseHandler db;
    public static AddNewClass newInstance() {
        return new AddNewClass();
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
        View view = inflater.inflate(R.layout.new_class, container, false);
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
        newClassName = requireView().findViewById(R.id.newClassName);
        newClassProfessor = requireView().findViewById(R.id.newClassProfessor);
        newClassSection = requireView().findViewById(R.id.newClassSection);
        newClassBuilding = requireView().findViewById(R.id.newClassBuilding);
        newClassRoomNumber = requireView().findViewById(R.id.newClassRoomNumber);
        newClassTime = requireView().findViewById(R.id.newClassTime);
        newClassDates = requireView().findViewById(R.id.newClassDates);
        newClassSaveButton = requireView().findViewById(R.id.newClassButtonCreate);

        db = new CollegeDatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String className = bundle.getString("Class name");
            String classProfessor = bundle.getString("professor");
            String classSection = bundle.getString("Class Section");
            String classBuilding = bundle.getString("Building");
            String classRoomNumber = bundle.getString("RoomNumber");
            String classTime = bundle.getString("time");
            String classDays = bundle.getString("days");
            newClassName.setText(className);
            newClassProfessor.setText(classProfessor);
            newClassSection.setText(classSection);
            newClassBuilding.setText(classBuilding);
            newClassRoomNumber.setText(classRoomNumber);
            newClassTime.setText(classTime);
            newClassDates.setText(classDays);
            assert className != null;
            assert classProfessor != null;
            assert classSection != null;
            assert classBuilding != null;
            assert classRoomNumber != null;
            assert classTime != null;
            assert classDays != null;
            if (classDays.length() > 0) {
                newClassSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
            }
        }
        newClassDates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newClassSaveButton.setEnabled(false);
                    newClassSaveButton.setTextColor(Color.GRAY);
                } else {
                    newClassSaveButton.setEnabled(true);
                    newClassSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        newClassSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newClassName.getText().toString();
                String professor = newClassProfessor.getText().toString();
                String section = newClassSection.getText().toString();
                String building = newClassBuilding.getText().toString();
                String roomNumber = newClassRoomNumber.getText().toString();
                String time = newClassTime.getText().toString();
                String days = newClassDates.getText().toString();
                if (finalIsUpdate) {
                    db.updateClass(bundle.getInt("id"), name, professor, section, building, roomNumber, time, days);
                } else {
                    CollegeClass thisClass = new CollegeClass();
                    thisClass.setName(name);
                    thisClass.setProfessor(professor);
                    thisClass.setClassSection(section);
                    thisClass.setBuilding(building);
                    thisClass.setRoomNumber(roomNumber);
                    thisClass.setTime(time);
                    thisClass.setClassDates(days);
                    db.insertClass(thisClass);
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
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }

}
