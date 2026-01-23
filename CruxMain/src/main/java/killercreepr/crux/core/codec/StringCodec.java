package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.StringDataNode;

public final class StringCodec implements Codec<String> {
    @Override
    public String decode(DataNode node) {
        if (!node.isString()) throw new DecodeException("Expected string");
        return node.asString();
    }

    @Override
    public DataNode encode(String value) {
        return new StringDataNode(value);
    }
}
