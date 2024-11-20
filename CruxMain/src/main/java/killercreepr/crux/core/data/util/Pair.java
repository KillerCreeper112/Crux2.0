package killercreepr.crux.core.data.util;

import java.util.Objects;

public class Pair<F, S> {
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

    protected final F first;
    protected final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public Pair<S, F> swap() {
        return of(this.second, this.first);
    }

    public Pair<F, S> withFirst(F first){
        return of(first, second);
    }

    public Pair<F, S> withSecond(S second){
        return of(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" + this.first + ", " + this.second + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair<?, ?> other)) return false;
        return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }
}

