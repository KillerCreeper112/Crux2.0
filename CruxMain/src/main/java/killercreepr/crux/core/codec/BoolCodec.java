package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.BooleanDataNode;

public final class BoolCodec implements Codec<Boolean> {
    @Override
    public Boolean decode(DataNode node) {
        if (!node.isBoolean()) throw new DecodeException("Expected boolean");
        return node.asBoolean();
    }

    @Override
    public DataNode encode(Boolean value) {
        return new BooleanDataNode(value);
    }
}
