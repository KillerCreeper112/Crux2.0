package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.core.codec.node.StringDataNode;

public final class EnumCodec<E extends Enum<E>> implements Codec<E> {
    private final Class<E> type;

    public EnumCodec(Class<E> type) {
        this.type = type;
    }

    @Override
    public E decode(DataNode node) {
        String name = new StringCodec().decode(node);
        try {
            return Enum.valueOf(type, name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new DecodeException("Invalid enum value: " + name);
        }
    }

    @Override
    public DataNode encode(E value) {
        return new StringDataNode(value.name().toLowerCase());
    }
}
