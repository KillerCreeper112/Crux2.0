package killercreepr.cruxconfig.config.common;

import killercreepr.cruxconfig.config.common.base.registry.FileObjectHandlerRegistry;
import killercreepr.cruxconfig.config.common.base.registry.FileParsedObjectRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.AutoFileHandler;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface FileRegistry {
    @NotNull
    FileElement serializeToFile(@NotNull Object object);
    <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o);
    <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o, @NotNull FileContext<?> context);
    <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o);
    <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o, @NotNull FileContext<?> context);
    @Nullable Object deserializeObjectFromFile(@NotNull FileElement o);
    <T extends FileObjectHandler<?>> void registerFileHandler(@NotNull Class<?> clazz, @NotNull T handler);

    default void registerFileHandler(@NotNull AutoFileHandler<?>... serializers){
        for(AutoFileHandler<?> d : serializers){
            registerFileHandler(d.getType(), d);
        }
    }

    @NotNull
    FileObjectHandlerRegistry getHandlerRegistry();
    @NotNull
    FileParsedObjectRegistry getParsedObjectRegistry();
}
