package xyz.thaddev.projectapis.computercontrolsystem.exceptions;

public class EmptyCommandException extends RuntimeException{
    public EmptyCommandException(int id) {
        super("Command (" + id + ") is Empty!");
    }
}
