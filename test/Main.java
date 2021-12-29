import org.sjr.JSONObj;
import org.sjr.codec.defaults.LocalDateTimeCodec;

import java.time.LocalDateTime;

public class Main {
    public static void main (String ...args) {
        var json = new JSONObj();
        json.setSupplier(LocalDateTimeCodec.INSTANCE);

        var now = LocalDateTime.now();
        var result = json.put("now", now);
        var decode = json.getAs("now", LocalDateTime.class);
        System.out.println();
    }
}
