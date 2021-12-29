package org.sjr.supplier;

import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;
import org.sjr.supplier.concat.ConcatCodecSupplier;
import org.sjr.supplier.concat.ConcatDecoderSupplier;

import java.util.Optional;

public interface JSONDecoderSupplier {
    <T> Optional<? extends JSONDecoder<T>> decoder (Class<T> clazz);

    default ConcatDecoderSupplier concat (JSONDecoderSupplier other) {
        return new ConcatDecoderSupplier(this, other);
    }
}
