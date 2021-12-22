package org.sjr;

import org.sjr.codec.JSONEncoder;

public class ResultEncoder<T> implements JSONEncoder<Result<T>> {
    final private JSONEncoder<T> encoder;

    public ResultEncoder (JSONEncoder<T> encoder) {
        this.encoder = encoder;
    }

    @Override
    public JSONObj encode (Result<T> value) {
        if (value.isError()) {
            return ExceptionEncoder.INSTANCE.encode(value.getError());
        }

        return encoder.encode(value.get());
    }
}
