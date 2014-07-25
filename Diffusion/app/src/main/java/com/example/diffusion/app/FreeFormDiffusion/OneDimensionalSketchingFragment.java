package com.example.diffusion.app.FreeFormDiffusion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.example.diffusion.app.R;
import android.support.v4.app.Fragment;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;


/**
 * Fragment that handles the sketching of the initial concentration profile
 * Holds a sketching area view, and buttons associated with the sketching operations
 *
 */
public class OneDimensionalSketchingFragment extends Fragment implements View.OnClickListener {

    /* Instance Variables */
    private OneDimensionalInitialConcentrationView mSketchAreaView;
    private Button mRefreshButton, mChangeParametersButton, mAnimateButton, mLoadPreviousButton;
    private ToggleButton mToggleLinesButton, mInterpolateButton;
    private int numberOfGridPoints = 20; //set initially
    private DataPoints[] dataPointArray;
    private SketchFragmentListener mSketchFragmentListener;
    private boolean linesOn;

    public interface SketchFragmentListener {
        public void openParameterDialog(); //the int will the R.id value of the fragment it was called from
        public void animateValues(float[] initialValues, float[] xValues,
                                  int sketchAreaHeight, int sketchAreaWidth,
                                  boolean linesOn);
        public void loadAnimationFragment(String filename);
    }//end of interface


    /* Create a new instance of the fragment using the numberOfGridPoints as a parameter */
    public static OneDimensionalSketchingFragment newInstance(int numberOfGridPoints){

        OneDimensionalSketchingFragment fragment = new OneDimensionalSketchingFragment();
        Bundle args = new Bundle();
        args.putInt("number_of_grid_points", numberOfGridPoints);
        fragment.setArguments(args);
        return fragment;

    }//end of newInstance method

    /* Needed blank constructor for the above method */
    public OneDimensionalSketchingFragment(){}

    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        try{
            mSketchFragmentListener = (SketchFragmentListener) act;
         }catch (ClassCastException e){
            System.out.println("Activity hasn't implemented the listener yet");
        }

    }//end of onAttach method

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            this.numberOfGridPoints = getArguments().getInt("number_of_grid_points");
        }//end of if statement

    }//end of onCreateMethod

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_one_dimensional_sketching, container, false);

        mSketchAreaView = (OneDimensionalInitialConcentrationView) v.findViewById(R.id.drawingView);

        //pass values to the sketching view on creation
        mSketchAreaView.getLayoutParams().width = ((int)(750/this.numberOfGridPoints))*this.numberOfGridPoints;
        mSketchAreaView.setNumberOfGridPoints(this.numberOfGridPoints);
        if(this.dataPointArray != null){
            mSketchAreaView.setDataPointArray(this.dataPointArray); //might have to make a copy of it
        }
        mSketchAreaView.setLinesOn(linesOn);

        mRefreshButton = (Button) v.findViewById(R.id.refresh_btn);
        mRefreshButton.setOnClickListener(this);

        mChangeParametersButton = (Button) v.findViewById((R.id.param_btn));
        mChangeParametersButton.setOnClickListener(this);

        mAnimateButton = (Button) v.findViewById(R.id.animate_btn);
        mAnimateButton.setOnClickListener(this);

        mToggleLinesButton = (ToggleButton) v.findViewById(R.id.toggle_lines_btn);
        mToggleLinesButton.setOnClickListener(this);
        linesOn = false;

        mLoadPreviousButton = (Button) v.findViewById(R.id.load_previous_btn);
        mLoadPreviousButton.setOnClickListener(this);

        return v;

    }//end of onCreateView method

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.refresh_btn:

                mSketchAreaView.startNewDrawing();
                break;

            case R.id.animate_btn:

                dataPointArray = mSketchAreaView.getDataPointArray();
                int sketchViewHeight = mSketchAreaView.getHeight();
                int sketchAreaWidth = mSketchAreaView.getWidth();
                float[] xValues = new float[numberOfGridPoints];
                float[] initialValues = new float[numberOfGridPoints];

                //loop through the values, transfer to two float arrays, and pass to the main activity
                for(int i=0; i<dataPointArray.length; i++){

                    xValues[i] = dataPointArray[i].getMidX();
                    initialValues[i] = dataPointArray[i].getYPosition();
                }//end of for loop

                mSketchFragmentListener.animateValues(initialValues, xValues,
                                                        sketchViewHeight, sketchAreaWidth,
                                                        linesOn);
                break;

            case R.id.param_btn: //open up the parameter dialogue fragment from the activity
                dataPointArray = mSketchAreaView.getDataPointArray();
                linesOn = mToggleLinesButton.isChecked();
                mSketchFragmentListener.openParameterDialog();
                break;

            case R.id.toggle_lines_btn:
                linesOn = ((ToggleButton) v).isChecked();
                mSketchAreaView.setLinesOn(linesOn);
                break;


            case R.id.load_previous_btn: //some of the below may need to be in try/catch

                //open dialog with number
                AlertDialog.Builder dialogue = new AlertDialog.Builder(this.getActivity());
                dialogue.setTitle("Load");
                dialogue.setMessage("Pick one to load:");
                final Spinner spinner = new Spinner(this.getActivity());


                //get the ArrayList from the data base
                FreeFormDataBaseHandler db = new FreeFormDataBaseHandler(this.getActivity());
                ArrayList<String> filenames = db.getAllNames();
                db.close();

                //add the ArrayList to the spinner
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, filenames);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        String selectedFile = parent.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                dialogue.setView(spinner);

                dialogue.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //load the animation activity from in here with the selected string
                        String filename = spinner.getSelectedItem().toString();
                        mSketchFragmentListener.loadAnimationFragment(filename);
                    }
                });

                dialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                dialogue.show();

                break;

        }//end of switch statement
    }



    /* Getters/Setters */
    public DataPoints[] getDataPointArray(){return this.dataPointArray;}

    public void setNumberOfGridPoints(int n){
        this.numberOfGridPoints = n;
        mSketchAreaView.setNumberOfGridPoints(n);

    }

    public int getSketchViewHeight(){
        return mSketchAreaView.getHeight();
    }

}//end of Fragment class
