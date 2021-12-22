package org.sjr;

import org.sjr.codec.JSONEncoder;
import java.util.Arrays;

public class ExceptionEncoder implements JSONEncoder<Exception> {
    final public static ExceptionEncoder INSTANCE = new ExceptionEncoder();
    private ExceptionEncoder () {}

    @Override
    public JSONObj encode (Exception value) {
        JSONObj result = new JSONObj();
        result.put("name", value.getMessage());
        result.put("stack_trace", new JSONArr(Arrays.stream(value.getStackTrace()).map(StackTraceElement::toString)));

        return result;
    }
}
