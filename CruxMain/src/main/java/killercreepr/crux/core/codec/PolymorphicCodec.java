package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.crux.core.codec.node.StringDataNode;

import java.util.Map;

public final class PolymorphicCodec<T> implements Codec<T> {
    private final String typeField;
    private final Map<String, Codec<? extends T>> codecs;

    public PolymorphicCodec(String typeField, Map<String, Codec<? extends T>> codecs) {
        this.typeField = typeField;
        this.codecs = codecs;
    }

    @Override
    public T decode(DataNode node) {
        if (!node.isObjectData()) {
            throw new DecodeException("Expected object for polymorphic codec");
        }

        DataObject obj = node.asObjectData();
        DataNode typeNode = obj.get(typeField);

        if (typeNode == null || !typeNode.isString()) {
            throw new DecodeException("Missing or invalid type field: " + typeField);
        }

        String type = typeNode.asString();
        Codec<? extends T> codec = codecs.get(type);

        if (codec == null) {
            throw new DecodeException("Unknown type: " + type);
        }

        return codec.decode(node);
    }

    @Override
    public DataNode encode(T value) {
        // Find codec by runtime class
        for (var entry : codecs.entrySet()) {
            Codec<? extends T> codec = entry.getValue();
            try {
                DataNode encoded = codec.encodeUnchecked(value);

                // Ensure type field is present
                encoded.asObjectData().put(typeField, new StringDataNode(entry.getKey()));
                return encoded;
            } catch (ClassCastException ignored) {
                // Not this codec
            }
        }

        throw new DecodeException("No codec registered for value: " + value.getClass());
    }
}