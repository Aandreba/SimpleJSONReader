package org.sjr.supplier.concat;

import org.sjr.codec.JSONDecoder;
import org.sjr.supplier.JSONDecoderSupplier;

import java.util.Optional;

public class ConcatDecoderSupplier implements JSONDecoderSupplier {
    final private JSONDecoderSupplier alpha, beta;

    public ConcatDecoderSupplier (JSONDecoderSupplier alpha, JSONDecoderSupplier beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public <T> Optional<? extends JSONDecoder<T>> decoder (Class<T> clazz) {
        var first = alpha.decoder(clazz);
        if (first.isEmpty()) {
            return beta.decoder(clazz);
        }

        return first;
    }
}
