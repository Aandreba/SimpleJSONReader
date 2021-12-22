import org.sjr.ExceptionEncoder;
import org.sjr.JSONObj;
import org.sjr.Result;
import org.sjr.ResultEncoder;
import org.sjr.codec.IdentityCodec;

public class Main {
    public static void main (String ...args) {
        var text = "{ name: \"Alex\", surname: \"Andreba\", pi: 3.14159 }";
        var json = Result.ofSupplier(() -> new JSONObj(text));

        var encoder = new ResultEncoder(IdentityCodec.INSTANCE);
        var parse = encoder.encode(json);
        System.out.println();
    }
}
