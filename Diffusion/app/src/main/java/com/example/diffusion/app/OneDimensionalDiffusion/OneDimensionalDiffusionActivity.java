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
    implements OneDimensionalSketchingFragment.SketchFragmentListener, OneDimensionInputParamentersFragment.InputParameterListener {

    private OneDimensionalSketchingFragment sketchingFragment;
    private OneDimensionInputParamentersFragment inputFragment;
    private OneDimensionalDiffusionModel diffusionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimensional_diffusion); //this might need changing

        //check that a fragment isn't already being used
        if(savedInstanceState==null){

            sketchingFragment = new OneDimensionalSketchingFragment();
            LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
            LinearLayout ll = new LinearLayout(this); ll.setId(12345);

            getSupportFragmentManager().beginTransaction().add(ll.getId(), sketchingFragment).commit();
            fragmentContainer.addView(ll);

        }//end of if

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
    public void onButtonClick(int i){
        switch(i){

            case R.id.sketching_fragment: //input parameter button
                LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
                LinearLayout ll = new LinearLayout(this); ll.setId(12345);

                getSupportFragmentManager().beginTransaction().replace(ll.getId(), new OneDimensionInputParamentersFragment()).addToBackStack(null).commit();
                fragmentContainer.addView(ll);
                break;

        }
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

    /*
     * onButtonClick method that gets called from the input parameters fragment
     */
    @Override
    public void onButtonClick(int location, int n){
        switch(location){
            case R.id.input_fragment:
                LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainer);
                LinearLayout ll = new LinearLayout(this); ll.setId(12345);
                getSupportFragmentManager().beginTransaction().replace(ll.getId(), OneDimensionalSketchingFragment.newInstance(n)).addToBackStack(null).commit();
                fragmentContainer.addView(ll);
                break;
        }//end of switch statement
    }//end of method

}//end of Activity class
