package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.JSONCodecSupplier;
import org.sjr.supplier.JSONDecoderSupplier;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;

public class JSONObj extends AbstractMap<String, Object> {
    final protected static JSONParser PARSER = new JSONParser();
    final protected JSONObject object;
    public Optional<JSONCodecSupplier> supplier;

    public JSONObj () {
        this.object = new JSONObject();
        this.supplier = Optional.empty();
    }

    public JSONObj (JSONObject object) {
        this.object = object;
        this.supplier = Optional.empty();
    }

    public JSONObj (String json) throws ParseException {
        this.object = (JSONObject) PARSER.parse(json);
        this.supplier = Optional.empty();
    }

    public JSONObj (Reader reader) throws IOException, ParseException {
        this.object = (JSONObject) PARSER.parse(reader);
        this.supplier = Optional.empty();
    }

    @Override
    public Set<Entry<String, Object>> entrySet () {
        return this.object.entrySet();
    }

    public Object get (String key) {
        return object.get(key);
    }

    public <T> Result<T> getAs (String key, Class<T> type, JSONDecoderSupplier supplier) {
        return Result.ofSupplier(() -> {
            var encoder = supplier.decoder(type).get();
            return encoder.decode(getObject(key).get());
        });
    }

    public <T> Result<T> getAs (String key, Class<T> type) {
        return Result.ofResultSupplier(() -> getAs(key, type, this.supplier.get()));
    }

    public Result<JSONObj> getObject (String key) {
        return _getAs(key, JSONObject.class)
                .map(JSONObj::new)
                .compute(x -> x.supplier = this.supplier);
    }

    public Result<JSONArr> getArray (String key) {
        return _getAs(key, JSONArray.class)
                .map(JSONArr::new)
                .compute(x -> x.supplier = this.supplier);
    }

    // GETTERS
    public Result<String> getString (String key) {
        return Result.ofSupplier(() -> get(key).toString());
    }

    public Result<Integer> getInt (String key) {
        return _getAs(key, Number.class).flatMap(Number::intValue);
    }

    public Result<Long> getLong (String key) {
        return _getAs(key, Number.class).flatMap(Number::longValue);
    }

    public Result<Float> getFloat (String key) {
        return _getAs(key, Number.class).flatMap(Number::floatValue);
    }

    public Result<Double> getDouble (String key) {
        return _getAs(key, Number.class).flatMap(Number::doubleValue);
    }

    public <T> Result<T> getDecodable (String key, JSONDecoder<T> decoder) {
        return getObject(key).flatMap(decoder::decode);
    }

    public Result<JSONObj[]> getObjectArray (String key) {
        return getArray(key).flatMap(x -> x.stream().map(y -> new JSONObj((JSONObject) y)).toArray(JSONObj[]::new));
    }

    public Result<JSONArr[]> getArrayArray (String key) {
        return getArray(key).flatMap(x -> x.stream().map(y -> new JSONArr((JSONArray) y)).toArray(JSONArr[]::new));
    }

    public Result<String[]> getStringArray (String key) {
        return getArray(key).map(x -> x.stream().map(Object::toString).toArray(String[]::new));
    }

    public Result<int[]> getIntArray (String key) {
        var _array = getArray(key);
        if (_array.isError()) {
            return new Result<>(_array.getError());
        }

        var array = _array.get();
        return Result.ofSupplier(() -> array.stream().map(x -> (Number) x).mapToInt(Number::intValue).toArray());
    }

    public Result<long[]> getLongArray (String key) {
        var _array = getArray(key);
        if (_array.isError()) {
            return new Result<>(_array.getError());
        }

        var array = _array.get();
        return Result.ofSupplier(() -> array.stream().map(x -> (Number) x).mapToLong(Number::longValue).toArray());
    }

    public Result<float[]> getFloatArray (String key) {
        var _array = getArray(key);
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

    public Result<double[]> getDoubleArray (String key) {
        var _array = getArray(key);
        if (_array.isError()) {
            return new Result<>(_array.getError());
        }

        var array = _array.get();
        return Result.ofSupplier(() -> array.stream().map(x -> (Number) x).mapToDouble(Number::doubleValue).toArray());
    }

    public <T> Result<T[]> getDecodableArray (String key, JSONDecoder<T> decoder) {
        return getArray(key).flatMap(array -> array.stream().map(x -> decoder.decode((JSONObject) x)).toArray(i -> (T[]) Array.newInstance(decoder.getTargetClass(), i)));
    }

    // SETTERS
    public void put (String key, String value) {
        this.object.put(key, value);
    }

    public void put (String key, int value) {
        this.object.put(key, value);
    }

    public void put (String key, long value) {
        this.object.put(key, value);
    }

    public void put (String key, float value) {
        this.object.put(key, value);
    }

    public void put (String key, double value) {
        this.object.put(key, value);
    }

    public void put (String key, boolean value) {
        this.object.put(key, value);
    }

    public void put (String key, JSONObject value) {
        this.object.put(key, value);
    }

    public void put (String key, JSONArray value) {
        this.object.put(key, value);
    }

    public void put (String key, JSONObj value) {
        this.object.put(key, value.object);
    }

    public void put (String key, JSONArr value) {
        this.object.put(key, value.array);
    }

    public <T> void put (String key, JSONEncoder<T> encoder, T value) {
        this.put(key, encoder.encode(value));
    }

    public void put (String key, String ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, int ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, long ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, float ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, double ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, boolean ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, JSONObject ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, JSONArray ...value) {
        this.object.put(key, new JSONArr(value));
    }

    public void put (String key, JSONObj...value) {
        this.object.put(key, Arrays.stream(value).map(x -> x.object).collect(JSONCollector.INSTANCE));
    }

    public void put (String key, JSONArr...value) {
        this.object.put(key, Arrays.stream(value).map(x -> x.array).collect(JSONCollector.INSTANCE));
    }

    public <T> void put (String key, JSONEncoder<T> encoder, T ...value) {
        var array = Arrays.stream(value)
                .map(x -> encoder.encode(x).object)
                .collect(JSONCollector.INSTANCE);

        this.put(key, array);
    }

    public <T> void put (String key, JSONEncoder<T> encoder, List<T> value) {
        var array = value.stream()
                .map(x -> encoder.encode(x).object)
                .collect(JSONCollector.INSTANCE);

        this.put(key, array);
    }

    // STRING
    public String toJSONString () {
        return this.object.toJSONString();
    }

    @Override
    public String toString() {
        return object.toString();
    }

    // PRIVATES
    <T> Result<T> _getAs (String key) {
        return Result.ofSupplier(() -> (T) get(key));
    }

    <T> Result<T> _getAs (String key, Class<T> type) {
        return _getAs(key);
    }
}
