package org.sjr.codec.defaults;

import org.sjr.JSONObj;
import org.sjr.codec.JSONCodec;

public class IdentityCodec implements JSONCodec<JSONObj> {
    final public static IdentityCodec INSTANCE = new IdentityCodec();
    private IdentityCodec () {}

    @Override
    public JSONObj encode (JSONObj value) {
        return value;
    }

    @Override
    public JSONObj decode(JSONObj json) {
        return json;
    }

    @Override
    public Class<JSONObj> getTargetClass() {
        return JSONObj.class;
    }
}
