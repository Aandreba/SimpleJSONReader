package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ArrayReader {
    final public JSONArray object;

    public ArrayReader (JSONArray object) {
        this.object = object;
    }

    public <T> T getAs (int pos) {
        return (T) object.get(pos);
    }

    public <T> T getAs (int pos, Class<T> type) {
        return (T) object.get(pos);
    }

    public ObjectReader getObject (int pos) {
        return new ObjectReader(getAs(pos, JSONObject.class));
    }

    public ArrayReader getArray (int pos) {
        return new ArrayReader(getAs(pos, JSONArray.class));
    }

    public String getString (int pos) {
        return object.get(pos).toString();
    }

    public Number getNumber (int pos) {
        return getAs(pos);
    }

    public int getInt (int pos) {
        return getNumber(pos).intValue();
    }

    public long getLong (int pos) {
        return getNumber(pos).longValue();
    }

    public float getFloat (int pos) {
        return getNumber(pos).floatValue();
    }

    public double getDouble (int pos) {
        return getNumber(pos).longValue();
    }
}
