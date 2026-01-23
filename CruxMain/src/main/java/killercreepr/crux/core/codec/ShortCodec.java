package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.NumberDataNode;

public final class ShortCodec implements Codec<Short> {
    @Override
    public Short decode(DataNode node) {
        if (!node.isNumber()) throw new DecodeException("Expected number");
        return node.asNumber().shortValue();
    }

    @Override
    public DataNode encode(Short value) {
        return new NumberDataNode(value);
    }
}
