package util;

public class Time {

    //stores the time when program ist started
    public static float timeStarted = System.nanoTime();
    //returns the run time in seconds
    public static float getTime() {return (float) ((System.nanoTime()-timeStarted) * 1E-9);}

}

