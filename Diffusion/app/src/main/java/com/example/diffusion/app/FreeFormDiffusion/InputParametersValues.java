package com.example.diffusion.app.FreeFormDiffusion;

/**
 * Class used to store the values for the input parameters, passed about between fragments and activity.
 *
 * Created by Sam on 20/07/2014.
 */
public class InputParametersValues {

    private int numberOfGridPoints;
    private String boundaryConditions;
    private int temperatureSeekBarValue;
    private double deltaT;

    /* Empty constructor for initial creation */
    public InputParametersValues(){
        this.numberOfGridPoints = 75;
        this.boundaryConditions = "Zero Flux";
        this.temperatureSeekBarValue = 50;
        this.deltaT = 0.9;
    }//end of empty constructor

    /* Standard Constructor */
    public InputParametersValues(int n, String bc, int t, double d){
        this.numberOfGridPoints = n;
        this.boundaryConditions = bc;
        this.temperatureSeekBarValue = t;
        this.deltaT = d;
    }//end of constructor

    public int getNumberOfGridPoints() {
        return numberOfGridPoints;
    }

    public void setNumberOfGridPoints(int numberOfGridPoints) {
        this.numberOfGridPoints = numberOfGridPoints;
    }

    public String getBoundaryConditions() {
        return boundaryConditions;
    }

    public void setBoundaryConditions(String boundaryConditions) {
        this.boundaryConditions = boundaryConditions;
    }

    public int getTemperatureSeekBarValue() {
        return temperatureSeekBarValue;
    }

    public void setTemperatureSeekBarValue(int temperatureSeekBarValue) {
        this.temperatureSeekBarValue = temperatureSeekBarValue;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
    }

}//end of class
