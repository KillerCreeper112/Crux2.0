package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.NumberDataNode;

public final class IntCodec implements Codec<Integer> {
    @Override
    public Integer decode(DataNode node) {
        if (!node.isNumber()) throw new DecodeException("Expected number");
        return node.asNumber().intValue();
    }

    @Override
    public DataNode encode(Integer value) {
        return new NumberDataNode(value);
    }
}
