package com.example.diffusion.app.FreeFormBinaryDiffusion;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.diffusion.app.R;

public class OneDimensionalDiffusionActivity extends FragmentActivity
    implements OneDimensionalSketchingFragment.SketchFragmentListener
             {

    private OneDimensionalSketchingFragment sketchingFragment;
    private OneDimensionalDiffusionModel diffusionModel;
    private InputParametersDialogueFragment inputParametersDialogueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimensional_diffusion);

        if(savedInstanceState==null){

            sketchingFragment = new OneDimensionalSketchingFragment();
            inputParametersDialogueFragment = InputParametersDialogueFragment.newInstance();
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
                                                                            linesOn));
        ft.addToBackStack(null);
        ft.commit();
        fragmentContainer.addView(ll);


    }//end of animateValues method



    /* Called from sketching fragment, when loading a previous value */
    @Override
    public void loadAnimationFragment(String filename){

        //get the rest of the information from the database regarding the filename
        FreeFormDataBaseHandler db = new FreeFormDataBaseHandler(this);
        ParametersForDatabaseStorage pm = db.getEntry(filename);
        db.close();
        if(pm!=null){

            /* load animation fragment with the values from pm */
            float[] initialValues = pm.getConcentrationValues();


            int numberOfGridPoints = initialValues.length;
            int sketchViewWidth = ((int)(750/numberOfGridPoints))*numberOfGridPoints;
            int sketchViewHeight = sketchingFragment.getSketchViewHeight();

            //generate x values;
            float[] xValues = new float[numberOfGridPoints];
            float runningTotal = 0;
            float spaceBetweenPoints = sketchViewWidth/numberOfGridPoints;
            for(int i=0; i<xValues.length; i++){

                float value = (float)(((runningTotal*2) + spaceBetweenPoints)/2.0);
                xValues[i] = value;
                runningTotal += spaceBetweenPoints;
            }//end of for loop



            //open up the new activity
            LinearLayout ll = new LinearLayout(this); ll.setId(12345);
            LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(ll.getId(), OneDimensionalAnimationFragment.newInstance(initialValues, xValues,
                    sketchViewHeight, sketchViewWidth,
                    false));
            ft.addToBackStack(null);
            ft.commit();
            fragmentContainer.addView(ll);

        }//end of if statement

    }//end of loadAnimationFragment

}//end of Activity class


/*
 * get rid of the class, and update to be values from the database, update this as we go along, then saving will be easier
 */