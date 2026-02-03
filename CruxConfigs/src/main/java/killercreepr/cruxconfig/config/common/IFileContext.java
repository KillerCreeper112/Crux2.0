package killercreepr.cruxconfig.config.common;

import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface IFileContext {
  @Nullable Object deserializeObjectRaw(@NotNull Type type, @NotNull FileElement from);

  /*@NotNull
  FileElement serializeToFile(@Nullable Object object);
  <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o);
  <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o);

  default <T> T deserializeFromFileOrDefault(@NotNull Type type, @Nullable FileElement o, T fallback){
    T got = deserializeFromFile(type, o);
    return got == null ? fallback : got;
  }
  default <T> T deserializeFromFileOrDefault(@NotNull Class<T> clazz, @Nullable FileElement o, T fallback){
    T got = deserializeFromFile(clazz, o);
    return got == null ? fallback : got;
  }*/
}
