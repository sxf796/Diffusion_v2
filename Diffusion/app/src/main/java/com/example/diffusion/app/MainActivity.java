package com.example.diffusion.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.diffusion.app.FreeFormDiffusion.OneDimensionalDiffusionActivity;


public class MainActivity extends Activity implements View.OnClickListener{

    /* Instance Variables */
    private TextView homeScreenTextView;
    private Button oneDimensionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate the instance variables
        homeScreenTextView = (TextView) findViewById(R.id.homeScreenTextView);
        oneDimensionButton = (Button) findViewById(R.id.oneDButton);
        oneDimensionButton.setOnClickListener(this);
    }//end of onCreate method


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }//end of onCreateOptionsMenu method

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
    }//end of onOptionsItemSelected method

    @Override
    public void onClick(View v){

        if(v.getId()==R.id.oneDButton){
            //if one dimension is selected, start the activity to do with one dimension
            Intent intent = new Intent(this, OneDimensionalDiffusionActivity.class);
            startActivity(intent);

        }//end of if one d button

        //button for the additional options goes here


    }//end of onClick method

}//end of MainActivity class
