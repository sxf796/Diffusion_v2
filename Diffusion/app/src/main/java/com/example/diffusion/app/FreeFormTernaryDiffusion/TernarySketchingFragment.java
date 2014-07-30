package com.example.diffusion.app.FreeFormTernaryDiffusion;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.diffusion.app.R;
import com.example.diffusion.app.SketchingDataPoints;

import java.util.Arrays;

/**
 * Fragment which handles the sketching event for the free form ternary diffusion animations
 *
 */
public class TernarySketchingFragment extends Fragment implements View.OnClickListener{


    /* Instance Variables */
    private TernaryFreeFormSketchingView mTernaryFreeFormSketchingView;
    private Button mRedRefreshButton, mBlueRefreshButton, mRefreshAllButton;
    private RadioGroup mColourSelectionRadioGroup;
    private RadioButton mRedRadioButton, mBlueRadioButton;
    private int numberOfGridPoints = 35;
    private SketchingDataPoints[] mFirstSketchingDataPointArray, mSecondSketchingDataPointArray;

    //will need to add in a listener for communication with the activity in the future as well

    /**
     * Static method for creating a new instance of the sketching fragment
     */
    public static TernarySketchingFragment newInstance(int numberOfGridPoints) {
        TernarySketchingFragment fragment = new TernarySketchingFragment();
        Bundle args = new Bundle();
        args.putInt("number_of_grid_points", numberOfGridPoints);
        fragment.setArguments(args);
        return fragment;
    }//end of newInstance method

    /* Required blank constructor */
    public TernarySketchingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.numberOfGridPoints = getArguments().getInt("number_of_grid_points");
        }
    }//end of onCreateMethod

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_ternary_sketching, container, false);

        mTernaryFreeFormSketchingView = (TernaryFreeFormSketchingView) v.findViewById(R.id.ternary_sketching_view);

        //set up the values associated with this view
        mTernaryFreeFormSketchingView.getLayoutParams().width = ((int)(750/this.numberOfGridPoints))*this.numberOfGridPoints;
        mTernaryFreeFormSketchingView.setNumberOfGridPoints(this.numberOfGridPoints);

        if(this.mFirstSketchingDataPointArray!=null){
            mTernaryFreeFormSketchingView.setBlueDataPointArray(Arrays.copyOf(mFirstSketchingDataPointArray, mFirstSketchingDataPointArray.length));
        }

        if(this.mSecondSketchingDataPointArray!=null){
            mTernaryFreeFormSketchingView.setRedDataPointArray(Arrays.copyOf(mSecondSketchingDataPointArray, mSecondSketchingDataPointArray.length));
        }

        mRedRefreshButton = (Button) v.findViewById(R.id.refresh_red_btn);
        mRedRefreshButton.setOnClickListener(this);

        mBlueRefreshButton = (Button) v.findViewById(R.id.refresh_blue_btn);
        mBlueRefreshButton.setOnClickListener(this);

        mRefreshAllButton = (Button) v.findViewById(R.id.refresh_all_btn);
        mRefreshAllButton.setOnClickListener(this);

        mColourSelectionRadioGroup = (RadioGroup) v.findViewById(R.id.ternary_sketching_radio_group);
        mRedRadioButton = (RadioButton) v.findViewById(R.id.radio_button_red);
        mBlueRadioButton = (RadioButton) v.findViewById(R.id.radio_button_blue);

        mColourSelectionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch(i){

                    case R.id.radio_button_red:
                        mTernaryFreeFormSketchingView.setCurrentlySketching("Red");
                        break;

                    case R.id.radio_button_blue:
                        mTernaryFreeFormSketchingView.setCurrentlySketching("Blue");
                        break;

                }//end of switch statement

            }//end of onCheckedChanged method
        });

        mBlueRadioButton.setChecked(true); mRedRadioButton.setChecked(false);

      return v;

    }//end of onCreateView method



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }//end of onAttach method

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v){

        switch(v.getId()){

            case R.id.refresh_blue_btn:
                mTernaryFreeFormSketchingView.resetBlueDataPoints();
                break;

            case R.id.refresh_red_btn:
                mTernaryFreeFormSketchingView.resetRedDataPoints();
                break;

            case R.id.refresh_all_btn:
                mTernaryFreeFormSketchingView.resetBothArrays();
                break;

        }//end of switch statement

    }//end of onClick method


}//end of fragment class
