package exceptions;

import java.io.IOException;

public interface inter{
    String t="";

    void checkOil();

    private void checkEngine(){
        System.out.println("In private method");
    }

    default void checkGas(){
        System.out.println("In default method");
    }

    default void checkType(){
        /*this method is default and it needs implementation*/
    }

    private void checkNumber() throws RuntimeException{
        /*this method is private and it needs implementation*/
    }

    static void checkEnv(){
        System.out.println("In static method");
    }

    static void checkITP(){
        /*this method is default and it needs implementation*/
    }

    private static void checkCooler(){
        /*this method is private and it needs implementation*/
    }



}
