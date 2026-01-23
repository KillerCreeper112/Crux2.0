package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.NumberDataNode;

public final class DoubleCodec implements Codec<Double> {
    @Override
    public Double decode(DataNode node) {
        if (!node.isNumber()) throw new DecodeException("Expected number");
        return node.asNumber().doubleValue();
    }

    @Override
    public DataNode encode(Double value) {
        return new NumberDataNode(value);
    }
}
