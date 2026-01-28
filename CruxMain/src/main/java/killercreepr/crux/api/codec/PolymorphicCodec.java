package killercreepr.crux.api.codec;

import org.jetbrains.annotations.ApiStatus;

public interface PolymorphicCodec<T> extends Codec<T> {
  <I extends T> Codec<I> registerWith(String type, Codec<I> codec);

  //mainly here for other plugins to add their own things
  @ApiStatus.Experimental
  PolymorphicCodec<T> register(String type, Codec<? extends T> codec);
}
