package org.sjr.codec;

import org.sjr.supplier.JSONCodecSupplier;
import java.util.Optional;

public interface JSONCodec<T> extends JSONEncoder<T>, JSONDecoder<T>, JSONCodecSupplier {
    @Override
    default <I> Optional<JSONCodec<I>> codec(Class<I> clazz) {
        var target = this.getTargetClass();
        if (clazz.isAssignableFrom(target) || target.isAssignableFrom(clazz)) {
            return Optional.of((JSONCodec<I>) this);
        }

        return Optional.empty();
    }

    @Override
    default <I> Optional<JSONDecoder<I>> decoder(Class<I> clazz) {
        return JSONDecoder.super.decoder(clazz);
    }

    @Override
    default <I> Optional<JSONEncoder<I>> encoder(Class<I> clazz) {
        return JSONEncoder.super.encoder(clazz);
    }
}
