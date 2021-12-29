package org.sjr;

import java.util.Optional;

class Couple<A, B> {
    public A alpha;
    public B beta;

    public Couple (A alpha, B beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public static <A,B> Optional<Couple<A, B>> flatten (Optional<A> alpha, Optional<B> beta) {
        if (alpha.isEmpty() || beta.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new Couple<>(alpha.get(), beta.get()));
    }

    public static <A,B> Optional<Couple<A, B>> flatten (A alpha, Optional<B> beta) {
        if (beta.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new Couple<>(alpha, beta.get()));
    }

    public static <A,B> Optional<Couple<A, B>> flatten (Optional<A> alpha, B beta) {
        if (alpha.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new Couple<>(alpha.get(), beta));
    }
}
