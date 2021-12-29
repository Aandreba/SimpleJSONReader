package org.sjr.supplier;

import org.sjr.codec.JSONEncoder;
import java.util.Optional;

public interface JSONEncoderSupplier {
    <T> Optional<? extends JSONEncoder<T>> encoder (Class<T> clazz);
}
