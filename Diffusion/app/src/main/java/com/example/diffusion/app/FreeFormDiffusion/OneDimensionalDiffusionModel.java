package com.example.diffusion.app.FreeFormDiffusion;

/**
 * Model class for One Dimensional Diffusion Equation.
 *
 *  NOTE: the values stored in the DataPoint array are actual values -
 *      before they are plotted the values will have to be scaled to the height of the animation view
 *
 * Created by Sam on 14/07/2014.
 */
public class OneDimensionalDiffusionModel {

    private InputParametersValues inputParametersValues; //stores users input parameter values

    private int numberOfGridPoints;
    private int totalGridPoints; //this includes the grid points that act as boundaries
    private int numberOfTimeSteps;

    private float[] plottingValues; //passed on creation
    private float[] solutionValues; //holds values that are used in the solution
    private int currentTimeStep; //holds the current time step

    //the parameters below are set automatically for now, will make them manual this week
    private double diffusionCoefficient;
    private double maxDiffusionCoefficient;
    private double deltaX;
    private double deltaT;

    private int animationViewHeight; //used for scaling the concentration values

    private float previousValue; //used in solution method


    /* Constructor */
    public OneDimensionalDiffusionModel(float[] plottingValues, int animationViewHeight, InputParametersValues ipv){

        this.animationViewHeight = animationViewHeight;

        this.inputParametersValues = ipv;

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

    public double getDeltaT() {return this.deltaT;}

    public float[] getSolutionValues(){return this.solutionValues;}

    public void setDiffusionCoefficient(double i) {
        this.diffusionCoefficient = ((i*maxDiffusionCoefficient)/100.0);
        if(diffusionCoefficient>=maxDiffusionCoefficient){diffusionCoefficient = maxDiffusionCoefficient-0.01;}
        if(diffusionCoefficient<=0) diffusionCoefficient = 0.01;
    }//end of setDC method


    /* Helper method for the constructor */
    public void setDataPointArray(float[] plottingValues){

        this.diffusionCoefficient = 0.5;

        this.plottingValues = plottingValues;

        this.numberOfGridPoints = plottingValues.length;
        this.totalGridPoints = this.numberOfGridPoints + 2;

        this.solutionValues = new float[totalGridPoints]; //to include the boundary values

        this.deltaX = 1.0/(numberOfGridPoints-1); //TODO check whether this needs to be total or number of grid points

        this.deltaT = ((deltaX*deltaX)/(2*diffusionCoefficient))*0.8;//inputParametersValues.getDeltaT(); //add this as an input parameter that can be changed


        for(int i=0; i<4; i++){
            System.out.println(" *********************  ");
        }

        this.maxDiffusionCoefficient = (deltaX*deltaX)/(2*deltaT);
        System.out.println("");
        System.out.println("Max value should be: " + maxDiffusionCoefficient);
        this.diffusionCoefficient = maxDiffusionCoefficient/2.0;
        System.out.println("currentValue is: " + diffusionCoefficient);
        System.out.println("");

        for(int i=0; i<4; i++){
            System.out.println(" *********************  ");
        }

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
//        solutionValues[0] = totalValue/totalGridPoints; solutionValues[solutionValues.length-1] = totalValue/totalGridPoints;
        solutionValues[0] = solutionValues[5]; solutionValues[solutionValues.length-1] = solutionValues[solutionValues.length-7];
        setPreviousValue(solutionValues[0]);
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
                solutionValues[i] = (float)(((diffusionCoefficient * deltaT)/(deltaX*deltaX)) *
                        (getPreviousValue() + solutionValues[i+1] - (2*currentValue)) + currentValue);
                setPreviousValue(pV);
            }//end of for loop
        setPreviousValue(solutionValues[0]);
        //update the boundary conditions
//        solutionValues[0] = solutionValues[5]; solutionValues[solutionValues.length-1] = solutionValues[solutionValues.length-7];

        updatePlottingValues();


            this.currentTimeStep++;


    }//end of solutionOneStep

    /* Helper method for solutionOneStep
     *    Loops through the solution array, and updates the plotting values ready for the
     *    next image in the animation
     */
    public void updatePlottingValues(){

        for(int i=0; i<plottingValues.length; i++){

            plottingValues[i] = animationViewHeight - solutionValues[i+1]  ;

        }//end of for loop

    }//end of updatePlottingValues method

    //TODO removed the scaled factor for now - see difference and then put back in

}//end of Model class
