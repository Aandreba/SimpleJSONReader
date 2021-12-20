package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class JSONArrayWrapper extends AbstractList<Object> {
    final public JSONArray array;

    public JSONArrayWrapper (JSONArray array) {
        this.array = array;
    }

    public JSONArrayWrapper (String json) throws ParseException {
        this.array = (JSONArray) JSONWrapper.parser.parse(json);
    }

    public JSONArrayWrapper (Reader reader) throws IOException, ParseException {
        this.array = (JSONArray) JSONWrapper.parser.parse(reader);
    }

    public int size () {
        return array.size();
    }

    public Object get (int pos) {
        return array.get(pos);
    }

    public <T> Optional<T> getAs (int pos) {
        Object val = get(pos);

        try {
            return Optional.of((T) val);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public <T> Optional<T> getAs (int pos, Class<T> type) {
        return getAs(pos);
    }

    public Optional<JSONWrapper> getObject (int pos) {
        return getAs(pos, JSONObject.class).map(JSONWrapper::new);
    }

    public Optional<JSONArrayWrapper> getArray (int pos) {
        return getAs(pos, JSONArray.class).map(JSONArrayWrapper::new);
    }

    public Optional<String> getString (int pos) {
        Object obj = get(pos);
        if (obj == null) {
            return Optional.empty();
        }

        return Optional.of(obj.toString());
    }

    public OptionalInt getInt (int pos) {
        return getAs(pos, Number.class).stream().mapToInt(Number::intValue).findFirst();
    }

    public OptionalLong getLong (int pos) {
        return getAs(pos, Number.class).stream().mapToLong(Number::longValue).findFirst();
    }

    public Optional<Float> getFloat (int pos) {
        return getAs(pos, Number.class).map(Number::floatValue);
    }

    public OptionalDouble getDouble (int pos) {
        return getAs(pos, Number.class).stream().mapToDouble(Number::doubleValue).findFirst();
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
