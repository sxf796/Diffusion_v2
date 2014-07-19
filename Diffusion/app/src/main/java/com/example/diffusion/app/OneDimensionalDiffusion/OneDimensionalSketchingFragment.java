package com.example.diffusion.app.OneDimensionalDiffusion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.diffusion.app.R;
import android.support.v4.app.Fragment;
import android.widget.ToggleButton;


/**
 * Fragment that handles the sketching of the initial concentration profile
 * Holds a sketching area view, and buttons associated with the sketching operations
 *
 */
public class OneDimensionalSketchingFragment extends Fragment implements View.OnClickListener {

    /* Instance Variables */
    private OneDimensionalInitialConcentrationView mSketchAreaView;
    private Button mRefreshButton, mChangeParametersButton, mAnimateButton;
    private ToggleButton mToggleLinesButton, mInterpolateButton;
    private int numberOfGridPoints = 20;
    private DataPoints[] dataPointArray; //get this from the view when done is clicked, and pass to the activity,
    private SketchFragmentListener mSketchFragmentListener;
    private boolean linesOn;

    public interface SketchFragmentListener {
        public void onButtonClick(int i); //the int will the R.id value of the fragment it was called from
        public void animateValues(float[] initialValues, float[] xValues,
                                  int sketchAreaHeight, int sketchAreaWidth,
                                  boolean linesOn);
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

        //TODO change the below, so that the width of the sketching view is a portion of the screen, rather than being predefined
        mSketchAreaView.getLayoutParams().width = ((int)(750/this.numberOfGridPoints))*this.numberOfGridPoints;
        mSketchAreaView.setNumberOfGridPoints(this.numberOfGridPoints); //this might throw null pointer exceptions


        mRefreshButton = (Button) v.findViewById(R.id.refresh_btn);
        mRefreshButton.setOnClickListener(this);

        mChangeParametersButton = (Button) v.findViewById((R.id.param_btn));
        mChangeParametersButton.setOnClickListener(this);

        mAnimateButton = (Button) v.findViewById(R.id.animate_btn);
        mAnimateButton.setOnClickListener(this);

        mToggleLinesButton = (ToggleButton) v.findViewById(R.id.toggle_lines_btn);
        mToggleLinesButton.setOnClickListener(this);
        linesOn = false;

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

            case R.id.param_btn:
                mSketchFragmentListener.onButtonClick(R.id.sketching_fragment);
                break;

            case R.id.toggle_lines_btn:
                linesOn = ((ToggleButton) v).isChecked();
                mSketchAreaView.setLinesOn(linesOn);
                break;



        }//end of switch statement
    }

    public DataPoints[] getDataPointArray(){return this.dataPointArray;}

}//end of Fragment class
