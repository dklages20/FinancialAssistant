package financial.assistant.utils.data;

public class NumberUtils {
    
    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    
}