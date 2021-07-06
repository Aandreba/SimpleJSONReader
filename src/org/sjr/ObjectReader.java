package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ObjectReader {
    final private static JSONParser parser = new JSONParser();

    final public JSONObject object;

    public ObjectReader(JSONObject object) {
        this.object = object;
    }

    public ObjectReader (String json) throws ParseException {
        this.object = (JSONObject) parser.parse(json);
    }

    public <T> T getAs (String key) {
        return (T) object.get(key);
    }

    public <T> T getAs (String key, Class<T> type) {
        return (T) object.get(key);
    }

    public ObjectReader getObject (String key) {
        return new ObjectReader(getAs(key, JSONObject.class));
    }

    public ArrayReader getArray (String key) {
        return new ArrayReader(getAs(key, JSONArray.class));
    }

    public String getString (String key) {
        return object.get(key).toString();
    }

    public Number getNumber (String key) {
        return getAs(key);
    }

    public int getInt (String key) {
        return getNumber(key).intValue();
    }

    public long getLong (String key) {
        return getNumber(key).longValue();
    }

    public float getFloat (String key) {
        return getNumber(key).floatValue();
    }

    public double getDouble (String key) {
        return getNumber(key).longValue();
    }
}
