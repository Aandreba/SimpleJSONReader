package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.JSONCodecSupplier;
import org.sjr.supplier.JSONDecoderSupplier;
import org.sjr.supplier.JSONEncoderSupplier;

import javax.swing.text.html.Option;
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

    public Optional<JSONCodecSupplier> getSupplier() {
        return supplier;
    }

    public void setSupplier (JSONCodecSupplier supplier) {
        this.supplier = Optional.of(supplier);
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

    public <T> Result<T[]> getAsArray (String key, JSONDecoderSupplier supplier) {
        return Result.ofSupplier(() -> {
            var array = this.getArray(key).get();
            var stream = array.stream()
                    .map(x -> new Couple<>((JSONObject) x, (JSONDecoder<Object>) supplier.decoder(x.getClass()).get()))
                    .map(x -> (T) x.beta.decode(x.alpha));

            return stream.toArray(i -> (T[]) Array.newInstance(array.get(0).getClass(), i));
        });
    }

    public <T> Result<T[]> getAsArray (String key) {
        return Result.ofResultSupplier(() -> getAsArray(key, this.supplier.get()));
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

    public Optional<Exception> put (String key, JSONEncoderSupplier supplier, Object value) {
        try {
            JSONEncoder<Object> encoder = (JSONEncoder<Object>) supplier.encoder(value.getClass()).get();
            this.put(key, encoder.encode(value));
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    public Optional<Exception> put (String key, Object value) {
        try {
            return this.put(key, this.supplier.get(), value);
        } catch (Exception e) {
            return Optional.of(e);
        }
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

    public Optional<Exception> put (String key, JSONEncoderSupplier supplier, Object ...value) {
        try {
            var array = Arrays.stream(value)
                    .map(x -> new Couple<>(x, (JSONEncoder<Object>) supplier.encoder(x.getClass()).get()))
                    .map(x -> x.beta.encode(x.alpha).object)
                    .collect(JSONCollector.INSTANCE);

            this.put(key, array);
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    public Optional<Exception> put (String key, Object ...value) {
        try {
            return this.put(key, this.supplier.get(), value);
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    public Optional<Exception> put (String key, JSONEncoderSupplier supplier, List<?> value) {
        try {
            var array = value.stream()
                    .map(x -> new Couple<>(x, (JSONEncoder<Object>) supplier.encoder(x.getClass()).get()))
                    .map(x -> x.beta.encode(x.alpha).object)
                    .collect(JSONCollector.INSTANCE);

            this.put(key, array);
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    public Optional<Exception> put (String key, List<?> value) {
        try {
            return this.put(key, this.supplier.get(), value);
        } catch (Exception e) {
            return Optional.of(e);
        }
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
