package com.example.diffusion.app.FreeFormTernaryDiffusion;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.diffusion.app.FreeFormBinaryDiffusion.InputParametersDialogueFragment;
import com.example.diffusion.app.R;

/*
 * Activity which handles the fragments associated with ternary free form diffusion
 */
public class TernaryFreeFormActivity extends FragmentActivity {

    private TernarySketchingFragment mTernarySketchingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ternary_free_form);

        if(savedInstanceState==null){

            mTernarySketchingFragment = new TernarySketchingFragment(); //TernarySketchingFragment.newInstance(35);
            LinearLayout fragmentContainer = (LinearLayout) findViewById(R.id.fragContainerTernary);
            LinearLayout ll = new LinearLayout(this); ll.setId(12345);

            getSupportFragmentManager().beginTransaction().add(ll.getId(), mTernarySketchingFragment).commit();
            fragmentContainer.addView(ll);

        }//end of if statement

    }//end of onCreate method


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ternary_free_form, menu);
        return true;
    }

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
    }

}//end of class
