package killercreepr.crux.api.codec.builder;

import killercreepr.crux.api.codec.Codec;
import killercreepr.crux.core.codec.MapCodec;

import java.util.Map;
import java.util.function.Supplier;

public class MapCodecBuilder<K, V> {
  public static <K, V> MapCodecBuilder<K, V> mapCodecBuilder(Supplier<Map<K, V>> mapSupplier) {
    var builder = new MapCodecBuilder<K, V>();
    builder.mapSupplier = mapSupplier;
    return builder;
  }
  public static <K, V> MapCodecBuilder<K, V> mapCodecBuilder(Class<K> keyType, Class<V> valueType) {
    return new MapCodecBuilder<>();
  }

  public MapCodecBuilder<K, V> mapSupplier(Supplier<Map<K, V>> mapSupplier){
    this.mapSupplier = mapSupplier;
    return this;
  }

  public MapCodecBuilder<K, V> keyCodec(Codec<K> keyCodec){
    this.keyCodec = keyCodec;
    return this;
  }

  public MapCodecBuilder<K, V> valueCodec(Codec<V> valueCodec){
    this.valueCodec = valueCodec;
    return this;
  }

  public Supplier<Map<K, V>> mapSupplier;
  public Codec<K> keyCodec;
  public Codec<V>  valueCodec;

  public MapCodec<K, V> build(){
    return new MapCodec<>(mapSupplier, keyCodec, valueCodec);
  }
}
