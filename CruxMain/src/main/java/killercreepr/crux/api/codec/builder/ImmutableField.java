package killercreepr.crux.api.codec.builder;

import killercreepr.crux.api.codec.Codec;

import java.util.function.Function;

public final class ImmutableField<T, V> {

  private final String name;
  private final Codec<V> codec;
  private final Function<T, V> getter;

  private final boolean optional;
  private final V defaultValue;

  public ImmutableField(
    String name,
    Codec<V> codec,
    Function<T, V> getter,
    boolean optional,
    V defaultValue
  ) {
    this.name = name;
    this.codec = codec;
    this.getter = getter;
    this.optional = optional;
    this.defaultValue = defaultValue;
  }

  public String name() {
    return name;
  }

  public Codec<V> codec() {
    return codec;
  }

  public Function<T, V> getter() {
    return getter;
  }

  public boolean optional() {
    return optional;
  }

  public V defaultValue() {
    return defaultValue;
  }
}