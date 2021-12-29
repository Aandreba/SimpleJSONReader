package org.sjr.supplier;

import org.sjr.codec.JSONCodec;
import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.concat.ConcatCodecSupplier;

import java.util.Optional;

public interface JSONCodecSupplier extends JSONEncoderSupplier, JSONDecoderSupplier {
    <T> Optional<JSONCodec<T>> codec(Class<T> clazz);

    default ConcatCodecSupplier concat (JSONCodecSupplier other) {
        return new ConcatCodecSupplier(this, other);
    }

    @Override
    default <T> Optional<? extends JSONEncoder<T>> encoder(Class<T> clazz) {
        return codec(clazz);
    }

    @Override
    default <T> Optional<? extends JSONDecoder<T>> decoder (Class<T> clazz) {
        return codec(clazz);
    }
}
