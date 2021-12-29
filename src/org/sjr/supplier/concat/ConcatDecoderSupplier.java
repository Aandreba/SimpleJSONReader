package org.sjr.supplier.concat;

import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.JSONDecoderSupplier;
import org.sjr.supplier.JSONEncoderSupplier;
import org.sjr.supplier.identity.IdentityDecoderSupplier;
import org.sjr.supplier.identity.IdentityEncoderSupplier;

import java.util.Optional;

public class ConcatDecoderSupplier implements JSONDecoderSupplier {
    final private JSONDecoderSupplier alpha, beta;

    public ConcatDecoderSupplier (JSONDecoderSupplier alpha, JSONDecoderSupplier beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public ConcatDecoderSupplier (JSONDecoder<?> alpha, JSONDecoder<?> beta) {
        this.alpha = new IdentityDecoderSupplier(alpha);
        this.beta = new IdentityDecoderSupplier(beta);
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
