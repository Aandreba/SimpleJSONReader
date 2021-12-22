package org.sjr;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

final public class Result<T> {
    public interface Constructor<T> {
        T init () throws Exception;
    }

    final private T value;
    final private Exception error;

    public Result (T value) {
        Objects.requireNonNull(value);
        this.value = value;
        this.error = null;
    }

    public Result (Exception error) {
        Objects.requireNonNull(error);
        this.value = null;
        this.error = error;
    }

    public static <T> Result<T> ofSupplier (Constructor<T> supplier) {
        try {
            return new Result<>(supplier.init());
        } catch (Exception e) {
            return new Result<>(e);
        }
    }

    public boolean isValue () {
        return value != null;
    }

    public boolean isError () {
        return error != null;
    }

    public T get () {
        if (value == null) {
            assert this.error != null;
            throw new NoSuchElementException("No value found", this.error);
        }

        return value;
    }

    public Exception getError () {
        if (error == null) {
            throw new NoSuchElementException("No error found");
        }

        return error;
    }

    public <O> Result<O> map (Function<T,O> mapper) {
        if (this.isError()) {
            return new Result<>(this.error);
        }

        return new Result<>(mapper.apply(this.value));
    }

    public <O> Result<O> flatMap (Function<T,O> mapper) {
        if (this.isError()) {
            return new Result<>(this.error);
        }

        return ofSupplier(() -> mapper.apply(this.value));
    }

    public Optional<T> toOptional () {
        return Optional.ofNullable(value);
    }

    public Optional<Exception> toOptionalError () {
        return Optional.ofNullable(error);
    }

    public Stream<T> stream () {
        if (isError()) {
            return Stream.empty();
        }

        return Stream.of(this.value);
    }
}
