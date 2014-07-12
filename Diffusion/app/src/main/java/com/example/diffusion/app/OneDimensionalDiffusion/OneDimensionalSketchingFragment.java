package com.example.diffusion.app.OneDimensionalDiffusion;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Fragment;
import com.example.diffusion.app.R;


/**
 * Fragment that handles the sketching of the initial concentration profile
 * Holds a sketching area view, and buttons associated with the sketching operations
 *
 */
public class OneDimensionalSketchingFragment extends Fragment {

    /* Instance Variables */
    private OneDimensionalSketchAreaView mSketchAreaView;
    private Button mRefreshButton, mDoneButton;
    private int numberOfGridPoints = 100; //set to initial value to prevent error (FOR NOW)
    private DataPoints[] dataPointArray; //get this from the view when done is clicked, and pass to the activity,
    //which will then transfer it to the animation section

    /* Create a new instance of the fragment using the numberOfGridPoints as a parameter */
    public static OneDimensionalSketchingFragment newInstance(int numberOfGridPoints){

        OneDimensionalSketchingFragment fragment = new OneDimensionalSketchingFragment();
        Bundle args = new Bundle();
        args.putInt("number_of_grid_points", numberOfGridPoints);
        fragment.setArguments(args);
        return fragment;

    }//end of newInstance method

    /* Needed blank constructor for the above method */
    public OneDimensionalSketchingFragment(){

    }//end of blank constructor



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            this.numberOfGridPoints = getArguments().getInt("number_of_grid_points");
            System.out.println("Setting number of grid points: " + this.numberOfGridPoints);

        } else System.out.println("number of grid points not set");
    }//end of onCreateMethod

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_one_dimensional_sketching, container, false);

        mSketchAreaView = (OneDimensionalSketchAreaView) v.findViewById(R.id.drawingView);
        mSketchAreaView.getLayoutParams().width = ((int)(900/this.numberOfGridPoints))*this.numberOfGridPoints;
        mSketchAreaView.setNumberOfGridPoints(this.numberOfGridPoints); //this might throw null pointer exceptions
        System.out.println("have sent the sketching view this many grid points: " + numberOfGridPoints);
        mRefreshButton = (Button) v.findViewById(R.id.refresh_btn);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when refresh click call the view to start the new drawing
                mSketchAreaView.startNewDrawing();
            }
        });

        mDoneButton = (Button) v.findViewById(R.id.done_btn);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle the done click in here - return to the input selection fragment
                // and pass back the array of data points
            }
        });
        return v;

    }//end of onCreateView method


}//end of Fragment class
