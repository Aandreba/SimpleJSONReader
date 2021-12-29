package org.sjr.codec;

import org.json.simple.JSONObject;
import org.sjr.JSONObj;
import org.sjr.supplier.JSONDecoderSupplier;

import java.util.Optional;

public interface JSONDecoder<T> extends JSONDecoderSupplier {
    T decode (JSONObj json);
    Class<T> getTargetClass ();

    default T decode (JSONObject json) {
        return decode(new JSONObj(json));
    }

    @Override
    default <I> Optional<JSONDecoder<I>> decoder (Class<I> clazz) {
        var target = this.getTargetClass();
        if (clazz.isAssignableFrom(target) || target.isAssignableFrom(clazz)) {
            return Optional.of((JSONDecoder<I>) this);
        }

        return Optional.empty();
    }
}
