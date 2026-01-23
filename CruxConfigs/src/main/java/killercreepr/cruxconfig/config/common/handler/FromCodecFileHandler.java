package killercreepr.cruxconfig.config.common.handler;

import killercreepr.crux.api.codec.Codec;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FromCodecFileHandler<T> implements FileObjectHandler<T> {
  protected final Codec<T> codec;

  public FromCodecFileHandler(Codec<T> codec) {
    this.codec = codec;
  }

  @Override
  public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NonNull T object) {
    return FileObject.fromDataNode(codec.encode(object));
  }

  @Override
  public @Nullable T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
    return codec.decode(e);
  }
}
