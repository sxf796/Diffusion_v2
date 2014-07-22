package com.example.diffusion.app.OneDimensionalDiffusion;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.diffusion.app.R;

public class OneDimensionalDiffusionActivity extends FragmentActivity
    implements OneDimensionalSketchingFragment.SketchFragmentListener,
               InputParametersDialogueFragment.InputDialogueFragmentListener {

    private OneDimensionalSketchingFragment sketchingFragment;
    private OneDimensionalDiffusionModel diffusionModel;
    private InputParametersDialogueFragment inputParametersDialogueFragment;
    private InputParametersValues inputParametersValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimensional_diffusion);

        if(savedInstanceState==null){

            sketchingFragment = new OneDimensionalSketchingFragment();
            inputParametersValues = new InputParametersValues();
            inputParametersDialogueFragment = InputParametersDialogueFragment.newInstance(inputParametersValues);
            LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
            LinearLayout ll = new LinearLayout(this); ll.setId(12345);

            getSupportFragmentManager().beginTransaction().add(ll.getId(), sketchingFragment).commit();
            fragmentContainer.addView(ll);

        }//end of if
        else{
            //handle a reload here - to be done this week
        }//end of else

    }//end of onCreate method


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.one_dimensional_diffusion, menu);
        return true;
    }//end of method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }//end of method

    /*
     * onButtonClick method that gets called from the sketching fragment
     * when the 'Change Parameters' button is clicked
     *
     * TODO Change the names of these functions for readability
     */
    @Override
    public void openParameterDialog(){

                LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
                LinearLayout ll = new LinearLayout(this); ll.setId(12345);

                getSupportFragmentManager().beginTransaction().replace(ll.getId(), inputParametersDialogueFragment).addToBackStack(null).commit();
                fragmentContainer.addView(ll);


    }//end of method

    /*
     * Called from the sketching fragment when the animation button is clicked
     *
     * Moves to the Animation Fragment, and passes the to ar
     */
    public void animateValues(float[] initialValues, float[] xValues,
                                int sketchAreaHeight, int sketchAreaWidth,
                                boolean linesOn){

        LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
        LinearLayout ll = new LinearLayout(this); ll.setId(12345);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(ll.getId(), OneDimensionalAnimationFragment.newInstance(initialValues, xValues,
                                                                           sketchAreaHeight, sketchAreaWidth,
                                                                            linesOn, inputParametersValues));
        ft.addToBackStack(null);
        ft.commit();
        fragmentContainer.addView(ll);


    }//end of animateValues method

    /* Method called when input parameter dialog fragment is clicked */
    @Override
    public void onCommitChangesClick(InputParametersValues ipv){

        //get the values from the passed ipv
        inputParametersValues.setNumberOfGridPoints(ipv.getNumberOfGridPoints());
        inputParametersValues.setBoundaryConditions(ipv.getBoundaryConditions());
        inputParametersValues.setTemperatureSeekBarValue(ipv.getTemperatureSeekBarValue());
        inputParametersValues.setDeltaT(ipv.getDeltaT());

        //switch to the input parameter fragment
        LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
        LinearLayout ll = new LinearLayout(this); ll.setId(12345);
        sketchingFragment.setNumberOfGridPoints(inputParametersValues.getNumberOfGridPoints());
        getSupportFragmentManager().beginTransaction().replace(ll.getId(), sketchingFragment).addToBackStack(null).commit();
        fragmentContainer.addView(ll);

    }//end of onCommitChangesClick button

}//end of Activity class
