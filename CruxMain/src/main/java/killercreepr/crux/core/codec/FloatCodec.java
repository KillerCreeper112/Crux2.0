package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.NumberDataNode;

public final class FloatCodec implements Codec<Float> {
    @Override
    public Float decode(DataNode node) {
        if (!node.isNumber()) throw new DecodeException("Expected number");
        return node.asNumber().floatValue();
    }

    @Override
    public DataNode encode(Float value) {
        return new NumberDataNode(value);
    }
}
