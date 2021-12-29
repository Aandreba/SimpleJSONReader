package org.sjr.supplier;

import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.concat.ConcatDecoderSupplier;
import org.sjr.supplier.concat.ConcatEncoderSupplier;

import java.util.Optional;

public interface JSONEncoderSupplier {
    <T> Optional<? extends JSONEncoder<T>> encoder (Class<T> clazz);

    default ConcatEncoderSupplier concat (JSONEncoderSupplier other) {
        return new ConcatEncoderSupplier(this, other);
    }
}
