package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Stream;

public class JSONObjectWrapper {
    final protected static JSONParser PARSER = new JSONParser();
    final public JSONObject object;

    public JSONObjectWrapper (JSONObject object) {
        this.object = object;
    }

    public JSONObjectWrapper (String json) throws ParseException {
        this.object = (JSONObject) PARSER.parse(json);
    }

    public JSONObjectWrapper(Reader reader) throws IOException, ParseException {
        this.object = (JSONObject) PARSER.parse(reader);
    }

    public int size () {
        return object.size();
    }

    public Object get (String key) {
        return object.get(key);
    }

    public <T> Optional<T> getAs (String key) {
        Object val = get(key);

        try {
            return Optional.of((T) val);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public <T> Optional<T> getAs (String key, Class<T> type) {
        return getAs(key);
    }

    public Optional<JSONObjectWrapper> getObject (String key) {
        return getAs(key, JSONObject.class).map(JSONObjectWrapper::new);
    }

    public Optional<JSONArrayWrapper> getArray (String key) {
        return getAs(key, JSONArray.class).map(JSONArrayWrapper::new);
    }

    // GETTERS
    public Optional<String> getString (String key) {
        Object obj = get(key);
        if (obj == null) {
            return Optional.empty();
        }

        return Optional.of(obj.toString());
    }

    public OptionalInt getInt (String key) {
        return getAs(key, Number.class).stream().mapToInt(Number::intValue).findFirst();
    }

    public OptionalLong getLong (String key) {
        return getAs(key, Number.class).stream().mapToLong(Number::longValue).findFirst();
    }

    public Optional<Float> getFloat (String key) {
        return getAs(key, Number.class).map(Number::floatValue);
    }

    public OptionalDouble getDouble (String key) {
        return getAs(key, Number.class).stream().mapToDouble(Number::doubleValue).findFirst();
    }

    private <T> Optional<Stream<T>> getArrayStreamAs (String key) {
        return getArray(key).flatMap(x -> {
            ArrayList<T> cache = new ArrayList<>();
            var anyError = x.parallelStream().anyMatch(y -> {
                try {
                    return !cache.add((T) y);
                } catch (Exception e) {
                    e.printStackTrace();
                   return true;
                }
            });

            if (anyError) {
                return Optional.empty();
            }

            var last = x.parallelStream()
                    .skip(cache.size())
                    .map(y -> (T) y);

            return Optional.of(Stream.concat(cache.parallelStream(), last).parallel());
        });
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
