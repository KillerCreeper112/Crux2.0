package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;

import java.util.function.Predicate;

public final class ValidatingCodec<T> implements Codec<T> {
    private final Codec<T> inner;
    private final Predicate<T> predicate;
    private final String message;

    public ValidatingCodec(Codec<T> inner, Predicate<T> predicate, String message) {
        this.inner = inner;
        this.predicate = predicate;
        this.message = message;
    }

    @Override
    public T decode(DataNode node) {
        T value = inner.decode(node);
        if (!predicate.test(value)) {
            throw new DecodeException(message);
        }
        return value;
    }

    @Override
    public DataNode encode(T value) {
        return inner.encode(value);
    }
}
