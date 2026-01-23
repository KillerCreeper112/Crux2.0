package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.codec.node.StringDataNode;
import net.kyori.adventure.key.Key;

public final class KeyCodec implements Codec<Key> {
    @Override
    public Key decode(DataNode node) {
        if (!node.isString()) throw new DecodeException("Expected string");
        return Crux.key(node.asString());
    }

    @Override
    public DataNode encode(Key value) {
        return new StringDataNode(value.asString());
    }
}
