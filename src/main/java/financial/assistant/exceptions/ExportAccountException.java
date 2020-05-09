package financial.assistant.exceptions;

public class ExportAccountException extends Exception {
    
    public ExportAccountException() {
        super();
    }

    public ExportAccountException(String message) {
        super(message);
    }

    public ExportAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}