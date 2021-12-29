package org.sjr.supplier.identity;

import org.sjr.codec.JSONCodec;
import org.sjr.supplier.JSONCodecSupplier;

import java.util.Optional;

public class IdentityCodecSupplier<T> implements JSONCodecSupplier {
    final private JSONCodec<T> codec;

    public IdentityCodecSupplier(JSONCodec<T> codec) {
        this.codec = codec;
    }

    @Override
    public <I> Optional<JSONCodec<I>> get (Class<I> clazz) {
        var target = codec.getTargetClass();
        if (clazz.isAssignableFrom(target) || target.isAssignableFrom(clazz)) {
            return Optional.of((JSONCodec<I>) codec);
        }

        return Optional.empty();
    }
}
