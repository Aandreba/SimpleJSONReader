package org.sjr.codec;

import org.json.simple.JSONObject;
import org.sjr.JSONObj;

public interface JSONDecoder<T> {
    T decode (JSONObj json);
    Class<T> getTargetClass ();

    default T decode (JSONObject json) {
        return decode(new JSONObj(json));
    }
}
