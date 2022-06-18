package exceptions;

public class Empty extends Exception{
    public Empty(){
        super(String.format("You did not set a name!"));
    }
}
