package killercreepr.cruxconfig.config.common;

import killercreepr.cruxconfig.config.common.base.registry.FileObjectHandlerRegistry;
import killercreepr.cruxconfig.config.common.base.registry.FileParsedObjectRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Type;

public class WrappedFileRegistry implements FileRegistry {
  protected final FileRegistry fileRegistry;

  public WrappedFileRegistry(FileRegistry fileRegistry) {
    this.fileRegistry = fileRegistry;
  }

  public FileRegistry getFileRegistry() {
    return fileRegistry;
  }

  @Override
  public @NotNull Object deserializeObject(@NotNull Object o) {
    return fileRegistry.deserializeObject(o);
  }

  @Override
  public @Nullable Object deserializeObjectRaw(@NotNull Type type, @NotNull FileElement from, @NotNull FileContext<?> context) {
    return  fileRegistry.deserializeObjectRaw(type, from, context);
  }

  @Override
  public @NotNull FileElement serializeToFile(@Nullable Object object) {
    return fileRegistry.serializeToFile(object);
  }

  @Override
  public @NotNull FileElement serializeToFile(@Nullable Object object, @NotNull FileContext<?> context) {
    return fileRegistry.serializeToFile(object, context);
  }

  @Override
  public @org.jspecify.annotations.Nullable <T> T deserializeFromFile(@NotNull Type type, @Nullable FileElement o) {
    return fileRegistry.deserializeFromFile(type, o);
  }

  @Override
  public @org.jspecify.annotations.Nullable <T> T deserializeFromFile(@NotNull Type type, @Nullable FileElement o, @NotNull FileContext<?> context) {
    return fileRegistry.deserializeFromFile(type, o, context);
  }

  @Override
  public @org.jspecify.annotations.Nullable <T> T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o) {
    return fileRegistry.deserializeFromFile(clazz, o);
  }

  @Override
  public @org.jspecify.annotations.Nullable <T> T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o, @NotNull FileContext<?> context) {
    return fileRegistry.deserializeFromFile(clazz, o, context);
  }

  @Override
  public @Nullable Object deserializeObjectFromFile(@NotNull FileElement o) {
    return fileRegistry.deserializeObjectFromFile(o);
  }

  @Override
  public <T extends FileObjectHandler<?>> void registerFileHandler(@NotNull Class<?> clazz, @NonNull T handler) {
    fileRegistry.registerFileHandler(clazz, handler);
  }

  @Override
  public @NotNull FileObjectHandlerRegistry getHandlerRegistry() {
    return fileRegistry.getHandlerRegistry();
  }

  @Override
  public @NotNull FileParsedObjectRegistry getParsedObjectRegistry() {
    return fileRegistry.getParsedObjectRegistry();
  }
}
