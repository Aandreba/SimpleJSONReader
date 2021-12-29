package org.sjr.supplier.identity;

import org.sjr.codec.JSONCodec;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.JSONEncoderSupplier;

import java.util.Optional;

public class IdentityEncoderSupplier<T> implements JSONEncoderSupplier {
    final private JSONEncoder<T> encoder;

    public IdentityEncoderSupplier (JSONEncoder<T> codec) {
        this.encoder = codec;
    }

    @Override
    public <I> Optional<JSONEncoder<I>> encoder (Class<I> clazz) {
        var target = encoder.getTargetClass();
        if (clazz.isAssignableFrom(target) || target.isAssignableFrom(clazz)) {
            return Optional.of((JSONEncoder<I>) encoder);
        }

        return Optional.empty();
    }
}
