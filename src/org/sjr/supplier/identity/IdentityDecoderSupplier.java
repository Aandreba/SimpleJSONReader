package org.sjr.supplier.identity;

import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.JSONDecoderSupplier;
import org.sjr.supplier.JSONEncoderSupplier;

import java.util.Optional;

public class IdentityDecoderSupplier<T> implements JSONDecoderSupplier {
    final private JSONDecoder<T> encoder;

    public IdentityDecoderSupplier (JSONDecoder<T> codec) {
        this.encoder = codec;
    }

    @Override
    public <I> Optional<JSONDecoder<I>> decoder (Class<I> clazz) {
        var target = encoder.getTargetClass();
        if (clazz.isAssignableFrom(target) || target.isAssignableFrom(clazz)) {
            return Optional.of((JSONDecoder<I>) encoder);
        }

        return Optional.empty();
    }
}
