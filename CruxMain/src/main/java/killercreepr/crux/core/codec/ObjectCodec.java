package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.DecodeException;
import killercreepr.crux.api.codec.builder.field.Field;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.crux.core.codec.node.ObjectDataNode;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class ObjectCodec<T> implements Codec<T> {
    private final Supplier<T> constructor;
    private final List<Field<T, ?>> fields;

    public ObjectCodec(Supplier<T> constructor, List<Field<T, ?>> fields) {
        this.constructor = constructor;
        this.fields = fields;
    }

    @Override
    public T decode(DataNode node) {
        if (!node.isObjectData()) {
            throw new DecodeException("Expected object");
        }

        DataObject obj = node.asObjectData();
        T instance = constructor.get();

        for (Field<T, ?> f : fields) {
            DataNode child = obj.get(f.name);
            if (child == null) {
                if(!f.optional) throw new DecodeException("Missing field: " + f.name);
                else continue;
            }

            decodeField(instance, f, child);
        }

        return instance;
    }

    private <V> void decodeField(T instance, Field<T, V> f, DataNode node) {
        V value = f.codec.decode(node);
        f.setter.accept(instance, value);
    }

    @Override
    public DataNode encode(T value) {
        DataObject obj = new ObjectDataNode();

        for (Field<T, ?> f : fields) {
            encodeField(value, f, obj);
        }

        return obj;
    }

    private <V> void encodeField(T value, Field<T, V> f, DataObject obj) {
        V fieldValue = f.getter.apply(value);

        // Skip default values
        if (f.optional && Objects.equals(fieldValue, f.defaultValue)) {
            return;
        }

        obj.put(f.name, f.codec.encode(fieldValue));
    }
}
