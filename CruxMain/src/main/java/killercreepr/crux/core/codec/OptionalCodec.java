package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.node.DataNode;

public final class OptionalCodec<T> implements Codec<T> {
    private final Codec<T> inner;
    private final T defaultValue;

    public OptionalCodec(Codec<T> inner, T defaultValue) {
        this.inner = inner;
        this.defaultValue = defaultValue;
    }

    @Override
    public T decode(DataNode node) {
        if (node == null || node.isNull()) return defaultValue;
        return inner.decode(node);
    }

    @Override
    public DataNode encode(T value) {
        return inner.encode(value);
    }
}
