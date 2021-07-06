package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ArrayReader {
    final public JSONArray array;

    public ArrayReader (JSONArray array) {
        this.array = array;
    }

    public Object get (int pos) {
        return array.get(pos);
    }

    public <T> T getAs (int pos) {
        Object val = get(pos);
        return val == null ? null : (T) val;
    }

    public <T> T getAs (int pos, Class<T> type) {
        Object val = get(pos);
        return val == null ? null : (T) val;
    }

    public ObjectReader getObject (int pos) {
        return new ObjectReader(getAs(pos, JSONObject.class));
    }

    public ArrayReader getArray (int pos) {
        return new ArrayReader(getAs(pos, JSONArray.class));
    }

    public String getString (int pos) {
        Object obj = get(pos);
        return obj == null ? null : obj.toString();
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

    @Override
    public String toString() {
        return array.toString();
    }
}
