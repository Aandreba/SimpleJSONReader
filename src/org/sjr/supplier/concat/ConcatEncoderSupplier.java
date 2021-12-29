package org.sjr.supplier.concat;

import org.sjr.codec.JSONCodec;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.JSONCodecSupplier;
import org.sjr.supplier.JSONEncoderSupplier;
import org.sjr.supplier.identity.IdentityCodecSupplier;
import org.sjr.supplier.identity.IdentityEncoderSupplier;

import java.util.Optional;

public class ConcatEncoderSupplier implements JSONEncoderSupplier {
    final private JSONEncoderSupplier alpha, beta;

    public ConcatEncoderSupplier(JSONEncoderSupplier alpha, JSONEncoderSupplier beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public ConcatEncoderSupplier(JSONEncoder<?> alpha, JSONEncoder<?> beta) {
        this.alpha = new IdentityEncoderSupplier(alpha);
        this.beta = new IdentityEncoderSupplier(beta);
    }

    @Override
    public <T> Optional<? extends JSONEncoder<T>> encoder (Class<T> clazz) {
        var first = alpha.encoder(clazz);
        if (first.isEmpty()) {
            return beta.encoder(clazz);
        }

        return first;
    }
}
