package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

public class JSONArr extends AbstractList<Object> {
    final protected JSONArray array;

    public JSONArr () {
        this.array = new JSONArray();
    }

    protected JSONArr (boolean... list) {
        this();
        for (boolean val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (byte... list) {
        this();
        for (byte val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (short... list) {
        this();
        for (short val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (char... list) {
        this();
        for (char val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (int... list) {
        this();
        for (int val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (long... list) {
        this();
        for (long val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (float... list) {
        this();
        for (float val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (double... list) {
        this();
        for (double val: list) {
            this.array.add(val);
        }
    }

    protected JSONArr (Object... list) {
        this();
        Collections.addAll(this.array, list);
    }

    protected JSONArr (List<?> list) {
        if (list instanceof JSONArray) {
            this.array = (JSONArray) list;
        } else {
            this.array = new JSONArray();
            this.array.addAll(list);
        }
    }

    protected JSONArr (Stream<?> stream) {
        this();
        stream.forEach(this.array::add);
    }

    public JSONArr(String json) throws ParseException {
        this.array = (JSONArray) JSONObj.PARSER.parse(json);
    }

    public JSONArr(Reader reader) throws IOException, ParseException {
        this.array = (JSONArray) JSONObj.PARSER.parse(reader);
    }

    public int size () {
        return array.size();
    }

    public Object get (int pos) {
        return array.get(pos);
    }

    public <T> Result<T> getAs (int pos) {
        return Result.ofSupplier(() -> (T) get(pos));
    }

    public <T> Result<T> getAs (int pos, Class<T> type) {
        return getAs(pos);
    }

    public Result<JSONObj> getObject (int pos) {
        return getAs(pos, JSONObject.class).map(JSONObj::new);
    }

    public Result<JSONArr> getArray (int pos) {
        return getAs(pos, JSONArray.class).map(JSONArr::new);
    }

    // GETTERS
    public Result<String> getString (int pos) {
        return Result.ofSupplier(() -> get(pos).toString());
    }

    public Result<Integer> getInt (int pos) {
        return getAs(pos, Number.class).flatMap(Number::intValue);
    }

    public Result<Long> getLong (int pos) {
        return getAs(pos, Number.class).flatMap(Number::longValue);
    }

    public Result<Float> getFloat (int pos) {
        return getAs(pos, Number.class).flatMap(Number::floatValue);
    }

    public Result<Double> getDouble (int pos) {
        return getAs(pos, Number.class).flatMap(Number::doubleValue);
    }

    public <T> Result<T> getDecodable (int pos, JSONDecoder<T> decoder) {
        return getObject(pos).flatMap(decoder::decode);
    }

    public Result<JSONObj[]> getObjectArray (int pos) {
        return getArray(pos).flatMap(x -> x.stream().map(y -> (JSONObj) y).toArray(JSONObj[]::new));
    }

    public Result<JSONArr[]> getArrayArray (int pos) {
        return getArray(pos).flatMap(x -> x.stream().map(y -> (JSONArr) y).toArray(JSONArr[]::new));
    }

    public Result<String[]> getStringArray (int pos) {
        return getArray(pos).map(x -> x.stream().map(Object::toString).toArray(String[]::new));
    }

    public Result<int[]> getIntArray (int pos) {
        var _array = getArray(pos);
        if (_array.isError()) {
            return new Result<>(_array.getError());
        }

        var array = _array.get();
        return Result.ofSupplier(() -> array.stream().map(x -> (Number) x).mapToInt(Number::intValue).toArray());
    }

    public Result<long[]> getLongArray (int pos) {
        var _array = getArray(pos);
        if (_array.isError()) {
            return new Result<>(_array.getError());
        }

        var array = _array.get();
        return Result.ofSupplier(() -> array.stream().map(x -> (Number) x).mapToLong(Number::longValue).toArray());
    }

    public Result<float[]> getFloatArray (int pos) {
        var _array = getArray(pos);
        if (_array.isError()) {
            return new Result<>(_array.getError());
        }

        var array = _array.get();
        return Result.ofSupplier(() -> {
            float[] result = new float[array.size()];
            for (int i=0;i<result.length;i++) {
                result[i] = ((Number) array.get(i)).floatValue();
            }

            return result;
        });
    }

    public Result<double[]> getDoubleArray (int pos) {
        var _array = getArray(pos);
        if (_array.isError()) {
            return new Result<>(_array.getError());
        }

        var array = _array.get();
        return Result.ofSupplier(() -> array.stream().map(x -> (Number) x).mapToDouble(Number::doubleValue).toArray());
    }

    public <T> Result<T[]> getDecodableArray (int pos, JSONDecoder<T> decoder) {
        return getArray(pos).flatMap(array -> array.stream().map(x -> decoder.decode((JSONObject) x)).toArray(i -> (T[]) Array.newInstance(decoder.getTargetClass(), i)));
    }

    // SETTERS
    public void add (int pos, String value) {
        this.array.add(pos, value);
    }

    public void add (int pos, int value) {
        this.array.add(pos, value);
    }

    public void add (int pos, long value) {
        this.array.add(pos, value);
    }

    public void add (int pos, float value) {
        this.array.add(pos, value);
    }

    public void add (int pos, double value) {
        this.array.add(pos, value);
    }

    public void add (int pos, boolean value) {
        this.array.add(pos, value);
    }

    public void add (int pos, JSONObject value) {
        this.array.add(pos, value);
    }

    public void add (int pos, JSONArray value) {
        this.array.add(pos, value);
    }

    public void add (int pos, JSONObj value) {
        this.array.add(pos, value.object);
    }

    public void add (int pos, JSONArr value) {
        this.array.add(pos, value.array);
    }

    public <T> void add (int pos, JSONEncoder<T> encoder, T value) {
        this.add(pos, encoder.encode(value));
    }

    public void add (int pos, String ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, int ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, long ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, float ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, double ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, boolean ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, JSONObject ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, JSONArray ...value) {
        this.array.add(pos, new JSONArr(value));
    }

    public void add (int pos, JSONObj...value) {
        this.array.add(pos, Arrays.stream(value).map(x -> x.object).collect(JSONCollector.INSTANCE));
    }

    public void add (int pos, JSONArr...value) {
        this.array.add(pos, Arrays.stream(value).map(x -> x.array).collect(JSONCollector.INSTANCE));
    }

    public <T> void add (int pos, JSONEncoder<T> encoder, T ...value) {
        var array = Arrays.stream(value)
                .map(x -> encoder.encode(x).object)
                .collect(JSONCollector.INSTANCE);

        this.add(pos, array);
    }

    public <T> void add (int pos, JSONEncoder<T> encoder, List<T> value) {
        var array = value.stream()
                .map(x -> encoder.encode(x).object)
                .collect(JSONCollector.INSTANCE);

        this.add(pos, array);
    }

    // STRING
    public String toJSONString () {
        return this.array.toJSONString();
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
