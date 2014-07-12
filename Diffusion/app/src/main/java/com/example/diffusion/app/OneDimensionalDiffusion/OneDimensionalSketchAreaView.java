package com.example.diffusion.app.OneDimensionalDiffusion;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sam on 11/07/2014.
 */
public class OneDimensionalSketchAreaView extends View {

    /* Instance Variables */
    //drawing path
    private Path sketchPath;
    //drawing and canvas paint
    private Paint sketchPaint, linePaint, canvasPaint;
    //initial colour
    private int sketchingColour, lineColour;

    //canvas
    private Canvas sketchCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    //the size of the points on the grid
    private float pointSize, lineSize;

    private int numberOfGridPoints;
    private float spaceBetweenPoints;
    private DataPoints[] dataPointArray; //collected in this view and passed back to the fragment on complete
    private boolean dataPointArrayInitialised;

    /* Constructor */
    public OneDimensionalSketchAreaView(Context context, AttributeSet atrs){

        super(context, atrs);

        //call helper method to instantiate instance variables
        setUpView();

    }//end of constructor

    /* Helper method for constructor, used for initialising instance variables */
    public void setUpView(){

        sketchPath = new Path();//check if this is used
        sketchPaint = new Paint();

        pointSize = 5;

        //set the properties for the sketching (investigate the effects of these)
        sketchingColour = Color.BLUE;
        sketchPaint.setColor(sketchingColour);
        sketchPaint.setAntiAlias(true);
        sketchPaint.setStrokeWidth(pointSize);
        sketchPaint.setStyle(Paint.Style.STROKE);
        sketchPaint.setStrokeJoin(Paint.Join.ROUND);
        sketchPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG); //used for painting the grid

        //set the properties for the grid
        lineColour = -7829368;
        linePaint = new Paint();
        linePaint.setColor(lineColour);
        lineSize = 2f;
        linePaint.setStrokeWidth(lineSize);
        linePaint.setAlpha(100);

        //set up the array that will hold the coordinate value
        dataPointArrayInitialised = false;
    }//end of set up view method

    /* Getters/Setters for the instance variables */
    public void setNumberOfGridPoints(int n) {this.numberOfGridPoints = n;}

    public DataPoints[] getDataPointArray(){return this.dataPointArray;}

    /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /* Override Methods for the View Class */
     /* Code for handling setting up drawing surface */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){ //find out when this method gets called when a view gets created
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        if(h>0 && w>0){
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            sketchCanvas = new Canvas(canvasBitmap);
        }//end of if

    }//end of onSizeChanged method

    @Override
    public void onDraw(Canvas canvas){

//        super.onSizeChanged(w, h, oldw, oldh);
 //       canvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
   //     sketchCanvas = new Canvas(canvasBitmap);

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(sketchPath, sketchPaint);

        //for loop plots the grid lines onto the graph area
        for(int i=0; i<canvas.getWidth(); i+=(canvas.getWidth()/numberOfGridPoints)){ //change this to delta x at some point in the future
            //not sure if this works 100 percent just yet
            canvas.drawLine(i, 0, i, canvas.getHeight(), linePaint);
        }//end of for  loop

        //set up the data point array
        if(!dataPointArrayInitialised) setUpDataPointArray();

    }//end of onDraw method

    /* Used for setting up the data point array */
    public void setUpDataPointArray(){

        dataPointArray = new DataPoints[numberOfGridPoints];
        this.spaceBetweenPoints = sketchCanvas.getWidth()/numberOfGridPoints;

        float runningTotal = 0;
        //populate the array with data points
        for(int i=0; i<dataPointArray.length; i++){

            dataPointArray[i] = new DataPoints(runningTotal, runningTotal+spaceBetweenPoints);

            runningTotal += spaceBetweenPoints;
        }//end of for loop

        dataPointArrayInitialised = true;

        /* Testing printout below */


    }//end of setUpDataPointArray method

    /* Method for handling drawing to the screen */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        final int action = event.getAction();

        //retrieve the x and y positions of the users touch
        float touchX = event.getX();
        float touchY = event.getY();

        //determine the type of touch
        switch(action){

            case MotionEvent.ACTION_DOWN: //finger touching the screen
                sketchPath.moveTo(touchX, touchY);

            case MotionEvent.ACTION_MOVE: //finger moving across the screen

                addToScreen(touchX, touchY);

                //use historical coordinates to fill in gaps
                for(int j = 0; j < event.getHistorySize(); j++)
                {
                    for(int i = 0; i < event.getPointerCount(); i++)
                    {
                        float x = event.getHistoricalX(i, j);
                        float y = event.getHistoricalY(i, j);

                        addToScreen(x,y);
                    }
                }//end of for loop for historical coordinates

                break;

            case MotionEvent.ACTION_UP: //finger being lifted from the screen

                sketchCanvas.drawPath(sketchPath, sketchPaint);
                sketchPath.reset();
                break;

            default:

                return false; //return false for an unrecognised action

        }//end of switch statement

        invalidate();
        return true;

    }//end of onTouchEvent method

    /* Helper method for drawing to the screen */
    public void addToScreen(float x, float y){

        //find out which point of the screen the x and y positions belong to:
        DataPoints d;
        for(int i=0; i<dataPointArray.length; i++){
            d = dataPointArray[i];

            if(x>=d.getMinX() && x<=d.getMaxX()){
                //draw over the old point in white paint if it exists
                try{
                    sketchPaint.setColor(Color.WHITE);
                    sketchPaint.setStrokeWidth(6);
                    sketchCanvas.drawPoint(d.getMidX(), d.getYPosition(), sketchPaint);

                    sketchPaint.setColor(sketchingColour);
                    sketchPaint.setStrokeWidth(pointSize);
                }catch(NullPointerException npe) {}

                //paint in the new position,  and replace the new value in the array
                sketchCanvas.drawPoint(d.getMidX(), y, sketchPaint);
                d.setYPosition(y);

            }//end of if statement

        }//end of for loop

    }//end of addToScreen method

    /* Method called when the user refreshes the screen */
    public void startNewDrawing(){

        //empty the array
        dataPointArray = new DataPoints[numberOfGridPoints];
        dataPointArrayInitialised = false;

        //paint the canvas white, and then call the onDraw method to reset the screen
        sketchCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();

    }//end of startNewDrawing method

}//end of sketch area view class
