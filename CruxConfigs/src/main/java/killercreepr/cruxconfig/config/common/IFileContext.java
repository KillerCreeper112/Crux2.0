package killercreepr.cruxconfig.config.common;

import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface IFileContext {
  @Nullable Object deserializeObjectRaw(@NotNull Type type, @NotNull FileElement from);
}
