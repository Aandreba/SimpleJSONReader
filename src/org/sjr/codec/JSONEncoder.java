package org.sjr.codec;

import org.sjr.JSONObj;

public interface JSONEncoder<T> {
    JSONObj encode (T value);
}
