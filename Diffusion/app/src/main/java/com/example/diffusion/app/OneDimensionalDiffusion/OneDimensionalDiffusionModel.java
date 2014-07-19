package com.example.diffusion.app.OneDimensionalDiffusion;

/**
 * Model class for One Dimensional Diffusion Equation.
 *
 *  NOTE: the values stored in the DataPoint array are actual values -
 *      before they are plotted the values will have to be scaled to the height of the animation view
 *
 * Created by Sam on 14/07/2014.
 */
public class OneDimensionalDiffusionModel {

    private int numberOfGridPoints;
    private int totalGridPoints; //this includes the grid points that act as boundaries
    private int numberOfTimeSteps;

    private float[] plottingValues; //passed on creation
    private float[] solutionValues; //holds values that are used in the solution
    private int currentTimeStep; //holds the current time step

    //the parameters below are set automatically for now, will make them manual this week
    private double diffusionCoefficient;
    private double deltaX;
    private double deltaT;

    private int animationViewHeight; //used for scaling the concentration values

    private float previousValue; //used in solution method


    /* Constructor */
    public OneDimensionalDiffusionModel(float[] plottingValues, int animationViewHeight){

        //set the parameters manually
        this.diffusionCoefficient = 0.5;

        this.animationViewHeight = animationViewHeight;

        setDataPointArray(plottingValues);

    }//end of constructor

    /* ++++++++++++++++ Getters/Setters ++++++++++++++++++++++ */

    public void setNumberOfGridPoints(int numberOfGridPoints) {
        this.numberOfGridPoints = numberOfGridPoints;
    }

    public int getNumberOfGridPoints() {
        return numberOfGridPoints;
    }

    public void setNumberOfTimeSteps(int numberOfTimeSteps) {
        this.numberOfTimeSteps = numberOfTimeSteps;
    }

    public float getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(float previousValue) {
        this.previousValue = previousValue;
    }

    public int getNumberOfTimeSteps() {
        return numberOfTimeSteps;
    }

    public int getCurrentTimeStep(){return currentTimeStep;}

    public float[] getSolutionValues(){return this.solutionValues;}

    public void setDataPointArray(float[] plottingValues){

        this.plottingValues = plottingValues;

        this.numberOfGridPoints = plottingValues.length;
        this.totalGridPoints = this.numberOfGridPoints + 2;

        this.solutionValues = new float[totalGridPoints]; //to include the boundary values

        this.deltaX = 1.0/totalGridPoints; //TODO check whether this needs to be total or number of grid points

        this.deltaT = ((deltaX*deltaX)/diffusionCoefficient)*0.95; //add this as an input parameter that can be changed

        //call helper method to set up the arrays
        setUpSolutionArray();

    }//end of setDataPointArrayMethod

    public float[] getCurrentSolutionValues(){return this.solutionValues;}
    public float[] getPlottingValues(){return this.plottingValues;}

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /*
     * Takes into account the boundary conditions, and sets up the array of DataPoints that will be used
     *
     * FOR NOW: the boundary conditions will be the average value
     * IN FINAL ADDITIONAL: boundary conditions will be a chosen parameter
     */
    public void setUpSolutionArray(){

        float totalValue = 0.0f;

        //loop through plotting values, and add to solution values
        for(int i=0; i<plottingValues.length; i++){ //this shouldn't be -2 C

            float scaledValue =  animationViewHeight - plottingValues[i];

            solutionValues[i+1] = scaledValue;
            totalValue += scaledValue;
        }//end of for loop

        //set the boundary conditions to be half of the maximum value
        solutionValues[0] = totalValue/totalGridPoints; solutionValues[solutionValues.length-1] = totalValue/totalGridPoints;

        this.currentTimeStep = 0;

    }//end of setUpSolutionArray method

    /*
     * Calculates the next set of grid point values to be plotted
     * Returns a boolean stating whether the values have been modified
     */
    public void solutionOneStep(){

            float pV = 0f;
            float currentValue = 0f;

            //perform the calculations here
            for(int i=1; i<solutionValues.length-1; i++){

                pV = solutionValues[i]; //will be used in the next step of the calculation
                currentValue = solutionValues[i];
                solutionValues[i] = (float)(((diffusionCoefficient * deltaT)/deltaX) *
                        (getPreviousValue() + solutionValues[i+1] - (2*currentValue)) + currentValue);
                setPreviousValue(pV);
            }//end of for loop

            updatePlottingValues();

            this.currentTimeStep++;


    }//end of solutionOneStep

    /* Helper method for solutionOneStep
     *    Loops through the solution array, and updates the plotting values ready for the
     *    next image in the animation
     */
    public void updatePlottingValues(){

        for(int i=0; i<plottingValues.length; i++){

            plottingValues[i] = animationViewHeight - solutionValues[i+1]  ; //+ animation view height??????

        }//end of for loop

    }//end of updatePlottingValues method

    //TODO removed the scaled factor for now - see difference and then put back in

}//end of Model class
