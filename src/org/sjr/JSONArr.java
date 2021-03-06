package org.sjr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.JSONCodecSupplier;
import org.sjr.supplier.JSONDecoderSupplier;
import org.sjr.supplier.JSONEncoderSupplier;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

public class JSONArr extends AbstractList<Object> {
    final protected JSONArray array;
    public Optional<JSONCodecSupplier> supplier;

    public JSONArr () {
        this.array = new JSONArray();
        this.supplier = Optional.empty();
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

        this.supplier = Optional.empty();
    }

    protected JSONArr (Stream<?> stream) {
        this();
        stream.forEach(this.array::add);
    }

    public JSONArr(String json) throws ParseException {
        this.array = (JSONArray) JSONObj.PARSER.parse(json);
        this.supplier = Optional.empty();
    }

    public JSONArr(Reader reader) throws IOException, ParseException {
        this.array = (JSONArray) JSONObj.PARSER.parse(reader);
        this.supplier = Optional.empty();
    }

    public Optional<JSONCodecSupplier> getSupplier() {
        return supplier;
    }

    public void setSupplier (JSONCodecSupplier supplier) {
        this.supplier = Optional.of(supplier);
    }

    public int size () {
        return array.size();
    }

    public Object get (int pos) {
        return array.get(pos);
    }

    public <T> Result<T> getAs (int pos, Class<T> type, JSONDecoderSupplier supplier) {
        return Result.ofSupplier(() -> {
            var encoder = supplier.decoder(type).get();
            return encoder.decode(getObject(pos).get());
        });
    }

    public <T> Result<T> getAs (int pos, Class<T> type) {
        return Result.ofResultSupplier(() -> getAs(pos, type, this.supplier.get()));
    }

    public Result<JSONObj> getObject (int pos) {
        return _getAs(pos, JSONObject.class)
                .map(JSONObj::new)
                .compute(x -> x.supplier = this.supplier);
    }

    public Result<JSONArr> getArray (int pos) {
        return _getAs(pos, JSONArray.class)
                .map(JSONArr::new)
                .compute(x -> x.supplier = this.supplier);
    }

    // GETTERS
    public Result<String> getString (int pos) {
        return Result.ofSupplier(() -> get(pos).toString());
    }

    public Result<Integer> getInt (int pos) {
        return _getAs(pos, Number.class).flatMap(Number::intValue);
    }

    public Result<Long> getLong (int pos) {
        return _getAs(pos, Number.class).flatMap(Number::longValue);
    }

    public Result<Float> getFloat (int pos) {
        return _getAs(pos, Number.class).flatMap(Number::floatValue);
    }

    public Result<Double> getDouble (int pos) {
        return _getAs(pos, Number.class).flatMap(Number::doubleValue);
    }

    public Result<JSONObj[]> getObjectArray (int pos) {
        return getArray(pos).flatMap(x -> x.stream().map(y -> new JSONObj((JSONObject) y)).toArray(JSONObj[]::new));
    }

    public Result<JSONArr[]> getArrayArray (int pos) {
        return getArray(pos).flatMap(x -> x.stream().map(y -> new JSONArr((JSONArray) y)).toArray(JSONArr[]::new));
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

    public <T> Result<T[]> getAsArray (int pos, JSONDecoderSupplier supplier) {
        return Result.ofSupplier(() -> {
            var array = this.getArray(pos).get();
            var stream = array.stream()
                    .map(x -> new Couple<>((JSONObject) x, (JSONDecoder<Object>) supplier.decoder(x.getClass()).get()))
                    .map(x -> (T) x.beta.decode(x.alpha));

            return stream.toArray(i -> (T[]) Array.newInstance(array.get(0).getClass(), i));
        });
    }

    public <T> Result<T[]> getAsArray (int pos) {
        return Result.ofResultSupplier(() -> getAsArray(pos, this.supplier.get()));
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

    public Optional<Exception> add (int pos, JSONEncoderSupplier supplier, Object value) {
        try {
            JSONEncoder<Object> encoder = (JSONEncoder<Object>) supplier.encoder(value.getClass()).get();
            this.add(pos, encoder.encode(value));
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    public Optional<Exception> sadd (int pos, Object value) {
        try {
            return this.add(pos, this.supplier.get(), value);
        } catch (Exception e) {
            return Optional.of(e);
        }
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

    public Optional<Exception> add(int pos, JSONEncoderSupplier supplier, Object ...value) {
        try {
            var array = Arrays.stream(value)
                    .map(x -> new Couple<>(x, (JSONEncoder<Object>) supplier.encoder(x.getClass()).get()))
                    .map(x -> x.beta.encode(x.alpha).object)
                    .collect(JSONCollector.INSTANCE);

            this.add(pos, array);
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    public Optional<Exception> add(int pos, Object ...value) {
        try {
            return this.add(pos, this.supplier.get(), value);
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    public Optional<Exception> add (int pos, JSONEncoderSupplier supplier, List<?> value) {
        try {
            var array = value.stream()
                    .map(x -> new Couple<>(x, (JSONEncoder<Object>) supplier.encoder(x.getClass()).get()))
                    .map(x -> x.beta.encode(x.alpha).object)
                    .collect(JSONCollector.INSTANCE);

            this.add(pos, array);
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    public Optional<Exception> add (int pos, List<?> value) {
        try {
            return this.add(pos, this.supplier.get(), value);
        } catch (Exception e) {
            return Optional.of(e);
        }
    }

    // STRING
    public String toJSONString () {
        return this.array.toJSONString();
    }

    @Override
    public String toString() {
        return array.toString();
    }

    // PRIVATE
    <T> Result<T> _getAs(int pos) {
        return Result.ofSupplier(() -> (T) get(pos));
    }

    <T> Result<T> _getAs(int pos, Class<T> type) {
        return _getAs(pos);
    }
}
