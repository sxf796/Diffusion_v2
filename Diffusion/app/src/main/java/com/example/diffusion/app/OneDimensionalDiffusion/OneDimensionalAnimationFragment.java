package com.example.diffusion.app.OneDimensionalDiffusion;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.example.diffusion.app.MainActivity;
import com.example.diffusion.app.R;

import java.util.Arrays;

/**
 * Class which handles the animations of the diffusion activity
 *
 * Created by Sam on 15/07/2014.
 */
public class OneDimensionalAnimationFragment extends Fragment implements View.OnClickListener{

    /* Instance Variables */
    private AnimationView mAnimationView;
    private Button mRestartButton, mSnapShotButton;
    private ImageButton mPlayButton, mPauseButton;
    private ToggleButton mLinesToggleButton;
    private LinearLayout buttonLayout;

    private OneDimensionalDiffusionModel mDiffusionModel;
    private float[] initialValues, xValues, plottingValues;
    private int animationViewHeight, animationViewWidth;

    private boolean animationPlaying;
    private int playing; //set to 1 for playing, and 0 for paused
    private boolean linesOn;

    private PlayThread pt;

    /* Getters/Setters */
    public int getPlaying(){return this.playing;}

    public void setPlottingValues(float[] v) { this.plottingValues = v; }



    /* Static method for creating a new instance of the class */
    public static OneDimensionalAnimationFragment newInstance(float[] initialValues, float[] xValues,
                                                              int animationViewHeight, int animationViewWidth,
                                                                boolean linesOn){

        OneDimensionalAnimationFragment fragment = new OneDimensionalAnimationFragment();

        //attach the arguments to return fragment
        Bundle args = new Bundle();
        args.putFloatArray("initial_values", initialValues);
        args.putFloatArray("x_values", xValues);
        args.putInt("animation_view_height", animationViewHeight);
        args.putInt("animation_view_width", animationViewWidth);
        args.putBoolean("animation_lines_on", linesOn);
        fragment.setArguments(args);

        return fragment;

    }//end of newInstance method

    /* Blank constructor needed for the newInstance method */
    public OneDimensionalAnimationFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {

            this.initialValues = getArguments().getFloatArray("initial_values");
            setPlottingValues(Arrays.copyOf(initialValues, initialValues.length));
            this.xValues = getArguments().getFloatArray("x_values");
            this.animationViewHeight = getArguments().getInt("animation_view_height");
            this.animationViewWidth = getArguments().getInt("animation_view_width");
            this.linesOn = getArguments().getBoolean("animation_lines_on");
            this.animationPlaying = false;

        }//end of if statement

    }//end of onCreate method

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_one_dimensional_animation, container, false);

        //set up the AnimationView
        mAnimationView = (AnimationView) v.findViewById(R.id.animation_view);
        mAnimationView.setPlottingValues(this.plottingValues);
        mAnimationView.setInitialValues(this.initialValues);
        mAnimationView.getLayoutParams().height = this.animationViewHeight;
        mAnimationView.getLayoutParams().width = this.animationViewWidth;
        mAnimationView.setXValues(this.xValues);
        mAnimationView.setLinesOn(this.linesOn);

        buttonLayout = (LinearLayout) v.findViewById(R.id.animation_button_layout);

        mPlayButton = (ImageButton) new ImageButton(this.getActivity()); //might not work
        mPlayButton.setImageResource(R.drawable.play_button);
        mPlayButton.setId(12345);
        mPlayButton.setOnClickListener(this);

        buttonLayout.addView(mPlayButton);

        mPauseButton = (ImageButton) new ImageButton(this.getActivity());
        mPauseButton.setImageResource(R.drawable.pause_button);
        mPauseButton.setId(54321);
        mPauseButton.setOnClickListener(this);

        buttonLayout.addView(mPauseButton);
        mPauseButton.setVisibility(View.GONE);

        mRestartButton = (Button) v.findViewById(R.id.restart_btn);
        mRestartButton.setOnClickListener(this);

        mSnapShotButton = (Button) v.findViewById(R.id.snapshot_btn);
        mSnapShotButton.setOnClickListener(this);

        mLinesToggleButton = (ToggleButton) v.findViewById(R.id.toggle_lines_animation_btn);
        mLinesToggleButton.setOnClickListener(this);
        mLinesToggleButton.setChecked(linesOn);

        mDiffusionModel = new OneDimensionalDiffusionModel(this.plottingValues, this.animationViewHeight);

        return v;

    }//end of onCreateView method

    @Override
    public void onClick(View v){

        switch(v.getId()){

            case 12345:
                playAnimation();
                break;

            case 54321:
                pauseAnimation();
                break;

            case R.id.restart_btn:
                restartAnimation();
                break;

            case R.id.snapshot_btn:
                mAnimationView.createSnapShot();
                break;

            case R.id.toggle_lines_animation_btn:
                if(linesOn){
                    linesOn = false;
                }else if(!linesOn){
                    linesOn = true;
                }
                mAnimationView.setLinesOn(linesOn);
                break;

        }//end of switch statement

    }//end of onClick method

    /*
     * Inner Thread class, used for running the animations
     */
    class PlayThread extends Thread {

        public Handler handler = new Handler(); //change name to mHandler if this works

        public void run() {

            if(animationPlaying) {

                switch (getPlaying()) {
                    case 0: //0 is for paused
                        handler.postDelayed(this, 1);
                        break;
                    case 1: //1 is for playing
                        mDiffusionModel.solutionOneStep();
                        plottingValues = mDiffusionModel.getPlottingValues();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAnimationView.setPlottingValues(plottingValues);
                                mAnimationView.updateView();
                            }
                        });

                        handler.postDelayed(this, 1);
                        break;

                }//end of switch
            }//end of if statement

        }//end of run method

    }//end of inner PlayThread class



    /* Code which calls the runnable and plays the animation */
    public void playAnimation(){

        if(!animationPlaying){
            pt = new PlayThread();
            mPlayButton.setVisibility(View.GONE);
            mPauseButton.setVisibility(View.VISIBLE);
            this.playing = 1;
            this.animationPlaying = true;
            pt.start();
        }

    }//end of playAnimation method

    /* Called when the pause button is pressed */
    public void pauseAnimation(){

        if(playing==0){
            this.playing = 1;
            mPauseButton.setImageResource(R.drawable.pause_button);
        }
        else {
            this.playing = 0;
            mPauseButton.setImageResource(R.drawable.play_button);
        }

    }//end of pauseAnimation method

   /* Called when the restart animation is pressed */
    public void restartAnimation(){

        if(animationPlaying) {
            this.animationPlaying = false;
            pt = null;
            mAnimationView.restartAnimation(Arrays.copyOf(initialValues, initialValues.length));
            mDiffusionModel.setDataPointArray(Arrays.copyOf(initialValues, initialValues.length));
            mPauseButton.setImageResource(R.drawable.pause_button); playing = 1;
            mPauseButton.setVisibility(View.GONE);
            mPlayButton.setVisibility(View.VISIBLE);
        }//end of if

    }//end of restart animation method

}//end of animation fragment class
