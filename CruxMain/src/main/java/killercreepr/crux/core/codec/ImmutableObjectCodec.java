package killercreepr.crux.core.codec;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.api.codec.builder.ImmutableCodecBuilder;
import killercreepr.crux.api.codec.builder.ImmutableField;
import killercreepr.crux.api.codec.node.DataNode;
import killercreepr.crux.api.codec.node.DataObject;
import killercreepr.crux.core.codec.node.ObjectDataNode;

import java.util.List;

public final class ImmutableObjectCodec<T> implements Codec<T> {

  private final ImmutableCodecBuilder.ObjectConstructor<T> constructor;
  private final List<ImmutableField<T, ?>> fields;

  public ImmutableObjectCodec(
    ImmutableCodecBuilder.ObjectConstructor<T> constructor,
    List<ImmutableField<T, ?>> fields
  ) {
    this.constructor = constructor;
    this.fields = fields;
  }

  @Override
  public DataNode encode(T value) {
    DataObject obj = new ObjectDataNode();

    for (ImmutableField<T, ?> field : fields) {
      encodeField(obj, field, value);
    }

    return obj;
  }

  @SuppressWarnings("unchecked")
  private <V> void encodeField(
    DataObject obj,
    ImmutableField<T, V> field,
    T value
  ) {
    V fieldValue = field.getter().apply(value);

    obj.put(
      field.name(),
      field.codec().encode(fieldValue)
    );
  }

  @Override
  public T decode(DataNode element) {
    DataObject obj = element.asObjectData();

    Object[] args = new Object[fields.size()];

    for (int i = 0; i < fields.size(); i++) {
      ImmutableField<T, ?> field = fields.get(i);

      var fieldElement = obj.get(field.name());

      Object value;

      if (fieldElement == null || fieldElement.isNull()) {
        if (field.optional()) {
          value = field.defaultValue();
        } else {
          throw new IllegalStateException(
            "Missing required field: " + field.name()
          );
        }
      } else {
        value = field.codec().decode(fieldElement);
      }

      args[i] = value;
    }

    return constructor.create(args);
  }
}