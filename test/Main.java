import org.sjr.ExceptionEncoder;
import org.sjr.JSONObj;

public class Main {
    public static void main (String ...args) {
        var json = new JSONObj();
        var result = json.put(
                "null pointer", ExceptionEncoder.INSTANCE,
                new NullPointerException(), new ArithmeticException("Division by zero")
        );

        System.out.println();
    }
}
