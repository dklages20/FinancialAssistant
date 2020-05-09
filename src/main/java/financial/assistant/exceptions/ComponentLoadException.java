package financial.assistant.exceptions;

public class ComponentLoadException extends Exception{
    
    public ComponentLoadException() {
        super();
    }

    public ComponentLoadException(String message) {
        super(message);
    }

    public ComponentLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}