package killercreepr.cruxconfig.config.common;

import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileRegistry {
    @NotNull
    FileElement serializeToFileElement(@NotNull Object object);
    <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable FileElement o);
    <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable FileElement o, @NotNull FileContext<?> context);
    @Nullable Object deserializeObject(@NotNull FileElement o);
    <T extends FileHandler<?>> void registerHandler(@NotNull Class<?> clazz, @NotNull T handler);
}
