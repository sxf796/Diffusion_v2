package com.example.diffusion.app.OneDimensionalDiffusion;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;

import com.example.diffusion.app.R;


public class OneDimensionInputParamentersFragment extends Fragment implements View.OnClickListener{

    /* Instance variables for the view */
    private TextView mTimeStepTextView, mGridPointsTextView;
    private EditText mTimeStepEditText, mGridPointEditText;
    private int numberOfGridPoints, numberOfTimeSteps;
    private Button mRevertButton, mCommitButton;
    private InputParameterListener mInputParameterLister;

    //have a data structure, which holds the input parameters - might be a different class,

    public interface InputParameterListener{
        public void onButtonClick(int location, int numberOfGridPoints);
    }


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

    }//end of onCreate method

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_one_dimension_input_paramenters, container, false);

        mTimeStepTextView = (TextView) v.findViewById(R.id.timeStepTextView);
        mGridPointsTextView = (TextView) v.findViewById(R.id.gridPointTextView);
        mTimeStepEditText = (EditText) v.findViewById(R.id.timeStepEditText);
        mGridPointEditText = (EditText) v.findViewById(R.id.gridPointEditText);

        mRevertButton = (Button) v.findViewById(R.id.revert_btn);
        mRevertButton.setOnClickListener(this);

        mCommitButton = (Button) v.findViewById(R.id.commit_btn);
        mCommitButton.setOnClickListener(this);

        return v;

    }//end of onCreateView method

    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        try{
            mInputParameterLister = (InputParameterListener) act;
        }catch (ClassCastException e){
            System.out.println("Activity hasn't implemented the listener yet");
        }

    }//end of onAttach method

    @Override
    public void onClick(View v){

        switch(v.getId()){

            case R.id.revert_btn:
                //figure out how to implement the revert button in here - keep track of values
                break;

            case R.id.commit_btn:
                int n = Integer.parseInt(mGridPointEditText.getText().toString());
                mInputParameterLister.onButtonClick(R.id.input_fragment, n);
                break;

        }//end of switch statement

    }//end of onClick method


}//end of class


