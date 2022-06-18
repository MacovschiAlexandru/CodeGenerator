package exceptions;

import exceptions.CheckStuff;

public abstract class Car implements CheckStuff{
    private int EngineC;
    private int EngineP;
    private int topspeed;
    private int year;
    private int gasTank;
    private boolean isElectric;
    public Car(int EngineC, int EngineP, int topspeed, int year, int gasTank, boolean isElectric){
        this.EngineC = EngineC;
        this.EngineP = EngineP;
        this.topspeed = topspeed;
        this.year = year;
        this.gasTank = gasTank;
        this.isElectric = isElectric;

    }

    public int GetEngineC(){
        return EngineC;
    }

    public int GetEngineP(){
        return EngineP;
    }

    public int Gettopspeed(){
        return topspeed;
    }

    public int Getyear(){
        return year;
    }

    public int GetgasTank(){
        return gasTank;
    }

    public boolean GetisElectric(){
        return isElectric;
    }

    public void SetEngineC(int EngineC){
        this.EngineC = EngineC;
    }

    public void SetEngineP(int EngineP){
        this.EngineP = EngineP;
    }

    public void Settopspeed(int topspeed){
        this.topspeed = topspeed;
    }

    public void Setyear(int year){
        this.year = year;
    }

    public void SetgasTank(int gasTank){
        this.gasTank = gasTank;
    }

    public void SetisElectric(boolean isElectric){
        this.isElectric = isElectric;
    }
    public void justDoIt(){

        /*end of method*/}
    public abstract void stopCar();
    public abstract void startCar();

    public void checkOil(){///method declared in CheckStuff interface

    }

}