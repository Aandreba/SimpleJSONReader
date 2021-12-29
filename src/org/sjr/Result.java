package org.sjr;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

final public class Result<T> {
    public interface Constructor<T> {
        T init () throws Exception;
    }

    final private T value;
    final private Exception error;

    public Result (T value) {
        this.value = value;
        this.error = value == null ? new NullPointerException() : null;
    }

    public Result (Exception error) {
        this.value = null;
        this.error = error == null ? new NullPointerException() : error;
    }

    public static <T> Result<T> ofSupplier (Constructor<T> supplier) {
        try {
            return new Result<>(supplier.init());
        } catch (Exception e) {
            return new Result<>(e);
        }
    }

    public static <T> Result<T> ofResultSupplier (Constructor<Result<T>> supplier) {
        try {
            return supplier.init();
        } catch (Exception e) {
            return new Result<>(e);
        }
    }

    public static <T> Result<T> ofOptionalSupplier (Constructor<Optional<T>> supplier) {
        try {
            return ofOptional(supplier.init());
        } catch (Exception e) {
            return new Result<>(e);
        }
    }

    public static <T> Result<T> ofOptional (Optional<T> value) {
        if (value.isEmpty()) {
            return new Result<>(new NoSuchElementException("No value present"));
        }

        return new Result<>(value.get());
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
            return (Result<O>) this;
        }

        return new Result<>(mapper.apply(this.value));
    }

    public <O> Result<O> flatMap (Function<T, O> mapper) {
        if (this.isError()) {
            return (Result<O>) this;
        }

        return ofSupplier(() -> mapper.apply(this.value));
    }

    public <O> Result<O> flatMapResult (Function<T, Result<O>> mapper) {
        if (this.isError()) {
            return (Result<O>) this;
        }

        return mapper.apply(this.value);
    }

    public <O> Result<O> flatMapOptional (Function<T, Optional<O>> mapper) {
        if (this.isError()) {
            return (Result<O>) this;
        }

        return ofOptional(mapper.apply(this.value));
    }

    public Result<T> compute (Consumer<T> consumer) {
        if (this.isValue()) {
            consumer.accept(this.value);
        }

        return this;
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
