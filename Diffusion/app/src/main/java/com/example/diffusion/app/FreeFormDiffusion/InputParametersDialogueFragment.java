package com.example.diffusion.app.FreeFormDiffusion;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.diffusion.app.R;

import java.util.ArrayList;

/**
 * Pop-up dialogue fragment used for input parameters, opened from the
 * initial concentration profile sketching fragment
 *
 * Created by Sam on 19/07/2014.
 */
public class InputParametersDialogueFragment extends DialogFragment
   implements View.OnClickListener{

    private int numberOfGridPoints;
    private String selectedBoundaryCondition, committedBoundaryCondition;
    private int selectedPositionInBoundaryConditionSpinner, committedPositionInBoundaryConditonSpinner;
    private int temperatureSeekBarValue;
    private double deltaT;
    private InputParametersValues inputParametersValues;

    private EditText gridPointEditText, deltaTEditText;
    private Spinner boundaryConditionsSpinner;
    private SeekBar temperatureSeekBar;
    private Button revertButton, commitButton;

    private InputDialogueFragmentListener inputDialogueFragmentListener;

    /* Interface used for communication with the main activity */
    public interface InputDialogueFragmentListener {

        public void onCommitChangesClick(InputParametersValues ipv);

    }//end of interface


    /* Static method for generating a new instance of the dialogue fragment */
    public static InputParametersDialogueFragment newInstance(InputParametersValues ipv){

        InputParametersDialogueFragment fragment = new InputParametersDialogueFragment();
        Bundle args = new Bundle();
        args.putInt("number_of_grid_points", ipv.getNumberOfGridPoints());
        args.putString("boundary_conditions", ipv.getBoundaryConditions());
        args.putInt("temperature_seek_bar_value", ipv.getTemperatureSeekBarValue());
        args.putDouble("delta_t_value", ipv.getDeltaT());
        fragment.setArguments(args);
        return fragment;

    }//end of newInstance method

    /* Blank constructor needed for the above method */
    public InputParametersDialogueFragment() {}

    @Override
    public void onAttach(Activity act){

        super.onAttach(act);

        //try to attach the listener to the activity
        try{
            inputDialogueFragmentListener = (InputDialogueFragmentListener) act;
        }catch(ClassCastException e){
            System.out.println("Activity hasn't implemented the listener yet");
        }


    }//end of onAttach method


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
             this.numberOfGridPoints = getArguments().getInt("number_of_grid_points");
             this.selectedBoundaryCondition = getArguments().getString("boundary_conditions");
             this.temperatureSeekBarValue = getArguments().getInt("temperature_seek_bar_value");
             this.deltaT = getArguments().getDouble("delta_t_value");
        }

    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_input_parameters, container, false);

        gridPointEditText = (EditText) v.findViewById(R.id.grid_point_edit_text);

        boundaryConditionsSpinner = (Spinner) v.findViewById(R.id.boundary_condition_spinner);
        initialiseBoundaryConditionSpinner();
        boundaryConditionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                selectedPositionInBoundaryConditionSpinner = position;
                selectedBoundaryCondition = adapterView.getItemAtPosition(position).toString();

            }//end of onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        temperatureSeekBar = (SeekBar) v.findViewById(R.id.temperature_seek_bar);
        temperatureSeekBarValue = 50;

        deltaTEditText = (EditText) v.findViewById(R.id.deltaT_edit_text);

        revertButton = (Button) v.findViewById(R.id.revert_back_btn);
        revertButton.setOnClickListener(this);

        commitButton = (Button) v.findViewById(R.id.commit_changes_btn);
        commitButton.setOnClickListener(this);

        return v;

    }//end of onCreateView method

    /* Helper method for onCreateView - adds the boundary conditions to the spinner */
    public void initialiseBoundaryConditionSpinner(){

        ArrayList<String> boundaryConditionsList = new ArrayList<String>();
        boundaryConditionsList.add("Zero Flux");
        boundaryConditionsList.add("Constant Value");
        boundaryConditionsList.add("Reflective");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, boundaryConditionsList);
        boundaryConditionsSpinner.setAdapter(arrayAdapter);
        selectedPositionInBoundaryConditionSpinner = 0;
        committedPositionInBoundaryConditonSpinner = 0; //TODO get this to be the one that is initially passed
    }//end of helper method



    @Override
    public void onClick(View v){

        switch (v.getId()){

            case R.id.revert_back_btn: //reset to previous values
                gridPointEditText.setText(numberOfGridPoints+ "");
                boundaryConditionsSpinner.setSelection(committedPositionInBoundaryConditonSpinner);
                temperatureSeekBar.setProgress(temperatureSeekBarValue);
                deltaTEditText.setText(deltaT + "");
                break;

            case R.id.commit_changes_btn: //set new values, and pass back to the activity
                numberOfGridPoints = Integer.parseInt(gridPointEditText.getText().toString());
                temperatureSeekBarValue = temperatureSeekBar.getProgress();
                committedPositionInBoundaryConditonSpinner = selectedPositionInBoundaryConditionSpinner;
                committedBoundaryCondition = selectedBoundaryCondition;
                deltaT = Double.parseDouble(deltaTEditText.getText().toString());

                inputDialogueFragmentListener.onCommitChangesClick(new InputParametersValues(
                        numberOfGridPoints, committedBoundaryCondition, temperatureSeekBarValue, deltaT
                ));

                break;

        }//end of switch statement

    }//end of onClick method

}//end of class
