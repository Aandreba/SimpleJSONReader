package org.sjr;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class JSONCollector implements Collector<Object, JSONArr, JSONArr> {
    final public static JSONCollector INSTANCE = new JSONCollector();
    private JSONCollector () {}

    @Override
    public Supplier<JSONArr> supplier() {
        return JSONArr::new;
    }

    @Override
    public BiConsumer<JSONArr, Object> accumulator() {
        return (x, y) -> x.array.add(y);
    }

    @Override
    public BinaryOperator<JSONArr> combiner() {
        return (x, y) -> {
            var result = new JSONArr();
            result.addAll(x);
            result.addAll(y);

            return result;
        };
    }

    @Override
    public Function<JSONArr, JSONArr> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
