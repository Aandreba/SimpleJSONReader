package org.sjr.supplier;

import org.sjr.codec.JSONDecoder;
import org.sjr.codec.JSONEncoder;

import java.util.Optional;

public interface JSONDecoderSupplier {
    <T> Optional<? extends JSONDecoder<T>> decoder (Class<T> clazz);
}
