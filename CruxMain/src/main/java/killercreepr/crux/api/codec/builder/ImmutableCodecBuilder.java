package killercreepr.crux.api.codec.builder;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.core.codec.ImmutableObjectCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ImmutableCodecBuilder<T> {

  @FunctionalInterface
  public interface ObjectConstructor<T> {
    T create(Object[] args);
  }

  public static <T> ImmutableCodecBuilder<T> immutableCodecBuilder(
    ObjectConstructor<T> constructor
  ) {
    return new ImmutableCodecBuilder<>(constructor);
  }

  private final ObjectConstructor<T> constructor;
  private final List<ImmutableField<T, ?>> fields = new ArrayList<>();

  private ImmutableCodecBuilder(
    ObjectConstructor<T> constructor
  ) {
    this.constructor = constructor;
  }

  public <V> ImmutableCodecBuilder<T> field(
    String name,
    Codec<V> codec,
    Function<T, V> getter
  ) {
    fields.add(new ImmutableField<>(
      name,
      codec,
      getter,
      false,
      null
    ));

    return this;
  }

  public <V> ImmutableCodecBuilder<T> optionalField(
    String name,
    Codec<V> codec,
    V defaultValue,
    Function<T, V> getter
  ) {
    fields.add(new ImmutableField<>(
      name,
      codec,
      getter,
      true,
      defaultValue
    ));

    return this;
  }

  public Codec<T> build() {
    return new ImmutableObjectCodec<>(
      constructor,
      fields
    );
  }
}