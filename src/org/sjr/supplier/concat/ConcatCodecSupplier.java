package org.sjr.supplier.concat;

import org.sjr.codec.JSONCodec;
import org.sjr.supplier.JSONCodecSupplier;

import java.util.Optional;

public class ConcatCodecSupplier implements JSONCodecSupplier {
    final private JSONCodecSupplier alpha, beta;

    public ConcatCodecSupplier (JSONCodecSupplier alpha, JSONCodecSupplier beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public <T> Optional<JSONCodec<T>> codec(Class<T> clazz) {
        var first = alpha.codec(clazz);
        if (first.isEmpty()) {
            return beta.codec(clazz);
        }

        return first;
    }
}
