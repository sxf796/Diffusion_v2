package com.example.diffusion.app.FreeFormBinaryDiffusion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.diffusion.app.R;

import java.util.ArrayList;

import java.util.Arrays;

/**
 * Class which handles the animations of the diffusion activity
 *
 * Created by Sam on 15/07/2014.
 */
public class FreeFormBinaryAnimationFragment extends Fragment implements View.OnClickListener{

    /* Instance Variables */
    private AnimationView mAnimationView;
    private Button mRestartButton, mSnapShotButton, mSaveDataButton, mChangeParameters;
    private ImageButton mPlayButton, mPauseButton;
    private ToggleButton mLinesToggleButton;
    private LinearLayout buttonLayout;
    private SeekBar mTemperatureSeekBar;

    private int temperatureSeekBarValue = 50;

    private FreeFormBinaryModel mDiffusionModel;
    private float[] initialValues, xValues, plottingValues;
    private int animationViewHeight, animationViewWidth;

    private boolean animationPlaying;
    private int playing; //set to 1 for playing, and 0 for paused
    private boolean linesOn;

    private PlayThread pt;

    private String boundaryConditions;
    private ArrayList<String> boundaryConditionsArray;
    private int positionInBCArray;
    private double deltaTFactor;

    /* Getters/Setters */
    public int getPlaying(){return this.playing;}

    public void setPlottingValues(float[] v) { this.plottingValues = v; }

    /* Static method for creating a new instance of the class */
    public static FreeFormBinaryAnimationFragment newInstance(float[] initialValues, float[] xValues,
                                                              int animationViewHeight, int animationViewWidth,
                                                                boolean linesOn){

        FreeFormBinaryAnimationFragment fragment = new FreeFormBinaryAnimationFragment();

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
    public FreeFormBinaryAnimationFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(getArguments()!=null) {

            //associated the arguments with the instance variables
            this.initialValues = getArguments().getFloatArray("initial_values");
            setPlottingValues(Arrays.copyOf(initialValues, initialValues.length));

            this.xValues = getArguments().getFloatArray("x_values");
            this.animationViewHeight = getArguments().getInt("animation_view_height");
            this.animationViewWidth = getArguments().getInt("animation_view_width");
            this.linesOn = getArguments().getBoolean("animation_lines_on");
            this.animationPlaying = false;
            this.boundaryConditions = "Constant Value";
            this.deltaTFactor = 0.8;
            this.boundaryConditionsArray = new ArrayList<String>();
            this.boundaryConditionsArray.add("Constant Value"); this.boundaryConditionsArray.add("Zero Flux");
            this.boundaryConditionsArray.add("Periodic"); this.positionInBCArray = 0;


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

        mPlayButton = new ImageButton(this.getActivity());
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

        mDiffusionModel = new FreeFormBinaryModel(this.plottingValues, this.animationViewHeight, boundaryConditions, deltaTFactor);

        mTemperatureSeekBar = (SeekBar) v.findViewById(R.id.animation_temperature_seek_bar);
        mTemperatureSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                mDiffusionModel.setDiffusionCoefficient(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSaveDataButton = (Button) v.findViewById(R.id.save_data_btn);
        mSaveDataButton.setOnClickListener(this);

        mChangeParameters = (Button) v.findViewById(R.id.change_parameters_btn);
        mChangeParameters.setOnClickListener(this);

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                if(i==KeyEvent.KEYCODE_BACK){

                    System.out.println("************");
                    System.out.println("BACK KEY PRESSED");
                    System.out.println("************");
                    if(pt!=null){

                    }
                }}
                return false;
            }
        });

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
                addSnapShotToTextView();
                break;

            case R.id.toggle_lines_animation_btn:
                if(linesOn){
                    linesOn = false;
                }else if(!linesOn){
                    linesOn = true;
                }
                mAnimationView.setLinesOn(linesOn);
                break;

            case R.id.save_data_btn:
                saveCurrentData();
                break;

            case R.id.change_parameters_btn:
                changeParameters();
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

                        int delay = (int)(((100 - (temperatureSeekBarValue))+1)*0.7);
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
            mTemperatureSeekBar.setProgress(50);
            pt = null;
            mAnimationView.restartAnimation(Arrays.copyOf(initialValues, initialValues.length));
            mDiffusionModel.restartAnimation();
            mPauseButton.setImageResource(R.drawable.pause_button); playing = 1;
            mPauseButton.setVisibility(View.GONE);
            mPlayButton.setVisibility(View.VISIBLE);
        }//end of if

    }//end of restart animation method

    /* Called when the snap shot button is pressed */
    public void addSnapShotToTextView(){

       int time =  mDiffusionModel.getCurrentTimeStep();
       SnapShotValues ssv = mAnimationView.getLatestSnapShot();
       int colour = ssv.getColour();

        //TODO add the text that pops up when you push the time stamp button here

        }//end of add snap shot to text view method

    /* Called when the save data button is pressed */
    public void saveCurrentData(){

        //open up a dialogue to ask for the filename
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Save");
        builder.setMessage("Enter File Name:");
        final EditText et = new EditText(this.getActivity());
        builder.setView(et);

        // attempt at solution, from http://stackoverflow.com/questions/9053685/android-sqlite-saving-string-arr
        //   and http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked

        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialogue = builder.create();

        //open up a connection to the database
        final FreeFormValuesDataBaseHandler db = new FreeFormValuesDataBaseHandler(this.getActivity());

        dialogue.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {


                //get the positive and negative buttons, and override the onClick listeners for them

                Button saveButton = dialogue.getButton(AlertDialog.BUTTON_POSITIVE);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String filename = et.getText().toString();
                        int numberOfGridPoints = mDiffusionModel.getNumberOfGridPoints();
                        String boundaryConditions = mDiffusionModel.getBoundaryConditions(); //will also get this from the model in the future
                        double deltaTFactor = mDiffusionModel.getDeltaTFactor();

                        ParametersForDatabaseStorage pm = new ParametersForDatabaseStorage(filename, numberOfGridPoints, boundaryConditions, deltaTFactor, initialValues);
                        try{
                            db.addNewEntry(pm);

                            String toastText1 = filename + " saved";
                            Toast successToast = Toast.makeText(getActivity().getBaseContext(), toastText1, Toast.LENGTH_LONG);
                            successToast.setGravity(Gravity.TOP, 0, 250);
                            successToast.show();

                            db.close();
                            dialogue.dismiss();
                        }catch (Exception e){
                            //make a toast saying trying a new name

                            String toastText2 = "Error: " + filename + " already in use.\nPlease enter different name";

                            Toast errorToast = Toast.makeText(getActivity().getBaseContext(), toastText2, Toast.LENGTH_LONG);
                            errorToast.setGravity(Gravity.TOP, 0, 250);
                            errorToast.show();

                        }//end of try catch block

                    }//end of onClick method for the positive button
                });

                Button cancelButton = dialogue.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.close();
                        dialogue.dismiss();
                    }
                });

            }//end of onShow method
        });


        dialogue.show();


    }//end of save current data method

    /* Opens a dialog for the parameters to be changed in
     *  Code from:
     *     attempt at solution, from http://stackoverflow.com/questions/9053685/android-sqlite-saving-string-arr
     *     and http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
     */
    public void changeParameters(){

        AlertDialog.Builder dialogue = new AlertDialog.Builder(this.getActivity());
        dialogue.setTitle("Parameters");
        dialogue.setMessage("Change Parameters:");

        LinearLayout dialogueLayout = new LinearLayout(this.getActivity());
        dialogueLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout bcLL = new LinearLayout(this.getActivity());

        TextView bcTV = new TextView(this.getActivity()); bcTV.setText("Boundary Condition: ");

        Spinner boundaryConditionSpinner = new Spinner(this.getActivity()); //check if this is the correct was to create a spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, boundaryConditionsArray);
        boundaryConditionSpinner.setAdapter(adapter);
        boundaryConditionSpinner.setSelection(positionInBCArray);
        boundaryConditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionInBCArray = i;
                boundaryConditions = boundaryConditionsArray.get(positionInBCArray);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bcLL.addView(bcTV); bcLL.addView(boundaryConditionSpinner);

        LinearLayout deltaTLL = new LinearLayout(this.getActivity());
        deltaTLL.setOrientation(LinearLayout.HORIZONTAL);

        TextView deltaTTextView = new TextView(this.getActivity());
        deltaTTextView.setText("Delta T factor: ");
        final EditText deltaTEditText = new EditText(this.getActivity());
        deltaTEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        deltaTEditText.setText("" + deltaTFactor);

        deltaTLL.addView(deltaTTextView); deltaTLL.addView(deltaTEditText);

        dialogue.setPositiveButton("Commit Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                deltaTFactor = Double.parseDouble(deltaTEditText.getText().toString());

                //update the values in the model
                mDiffusionModel.setDeltaTFactor(deltaTFactor);
                mDiffusionModel.setBoundaryConditions(boundaryConditions);
                restartAnimation();
                if(deltaTFactor>0.8){
                    Toast warningToast = Toast.makeText(getActivity().getBaseContext(), "Warning: May be unstable!", Toast.LENGTH_LONG);
                    warningToast.setGravity(Gravity.TOP, 0, 250);
                    warningToast.show();
                }
            }
        });

        dialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dialogue.dismiss();
            }
        });

        dialogueLayout.addView(bcLL); dialogueLayout.addView(deltaTLL);
        dialogue.setView(dialogueLayout);
        dialogue.show();

    }//end of changeParameters method

    @Override
    public void onDetach(){
        super.onDetach();
        System.out.println("ON DETACH CALLED");
      //  pt = null;
        this.playing = 0;
    }//end of onDetach method


//    public void myOnBackKeyPressed(){
//        pt = null;
//    }//end of onKeyDownMethod

}//end of animation fragment class
