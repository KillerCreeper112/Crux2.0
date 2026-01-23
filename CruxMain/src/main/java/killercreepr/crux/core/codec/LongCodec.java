package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.NumberDataNode;

public final class LongCodec implements Codec<Long> {
    @Override
    public Long decode(DataNode node) {
        if (!node.isNumber()) throw new DecodeException("Expected number");
        return node.asNumber().longValue();
    }

    @Override
    public DataNode encode(Long value) {
        return new NumberDataNode(value);
    }
}
