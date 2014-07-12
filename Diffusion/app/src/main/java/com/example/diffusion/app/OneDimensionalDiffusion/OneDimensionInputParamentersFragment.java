package com.example.diffusion.app.OneDimensionalDiffusion;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.diffusion.app.R;


public class OneDimensionInputParamentersFragment extends Fragment {

    /* Instance variables for the view */
    private TextView mTimeStepTextView, mGridPointsTextView;
    private EditText mTimeStepEditText, mGridPointEditText;
    private Button mSketchButton, mAnimateButton;

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

        //Buttons and their onClick listeners
        mSketchButton = (Button) v.findViewById(R.id.sketch_btn);
        mSketchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("IM GETTING CALLED");
                //get the number from the number grid edit text
                int numberOfGridPoints = Integer.parseInt(mGridPointEditText.getText().toString());
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

                OneDimensionalSketchingFragment odsf = OneDimensionalSketchingFragment.newInstance(numberOfGridPoints);
              //  OneDimensionalSketchingFragment o = new OneDimensionalSketchingFragment();
                mFragmentTransaction.replace(R.id.input_fragment, odsf);
                mFragmentTransaction.addToBackStack("input_fragment"); //change this so it actually works
                mFragmentTransaction.commit();
            }
        });

        mAnimateButton = (Button) v.findViewById(R.id.animate_btn);
        mAnimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to the animate screen here, figure out how to do this after it is working
            }
        });

        return v;

    }//end of onCreateView method



}//end of class


