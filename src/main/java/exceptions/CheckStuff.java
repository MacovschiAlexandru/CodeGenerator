package exceptions;

public interface CheckStuff {
    public String t="";

    default void checkGas(){
        /*this method is default and it needs implementation*/
    }

    private void checkEngine(){
        /*this method is private and it needs implementation*/
    }

    static void checkITP(){
        /*this method is default and it needs implementation*/
    }

    default void checkType(){
        /*this method is default and it needs implementation*/
    }

    private void checkNumber()throws RuntimeException{
        /*this method is private and it needs implementation*/
    }

    static void checkEnv(){
        /*this method is default and it needs implementation*/
    }

    public void checkOil();

    private static void checkCooler(){
        /*this method is private and it needs implementation*/
    }

}
