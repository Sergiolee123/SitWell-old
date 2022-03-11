package com.fyp.sitwell.mucleTraining;

public class RepeatCounter {
    private int counter;
    private Boolean half;

    public RepeatCounter(){
        this.counter = 0;
        this.half = false;
    }

    public int getCounter(){
        return counter;
    }

    public boolean addCounter(){

        if(half){
            counter++;
            half = false;
            return true;
        }
        return false;

    }

    public void finishedHalf(){
        half = true;
    }
}
