package org.sjr.codec;

import org.sjr.JSONObj;
import org.sjr.supplier.JSONEncoderSupplier;

import java.util.Optional;

public interface JSONEncoder<T> extends JSONEncoderSupplier {
    JSONObj encode (T value);
    Class<T> getTargetClass ();

    @Override
    default <I> Optional<JSONEncoder<I>> encoder (Class<I> clazz) {
        var target = this.getTargetClass();
        if (clazz.isAssignableFrom(target) || target.isAssignableFrom(clazz)) {
            return Optional.of((JSONEncoder<I>) this);
        }

        return Optional.empty();
    }
}
