package killercreepr.cruxconfig.config.common;

import killercreepr.cruxconfig.config.common.base.registry.FileObjectHandlerRegistry;
import killercreepr.cruxconfig.config.common.base.registry.FileParsedObjectRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.AutoFileHandler;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.function.Supplier;

public interface FileRegistry {
    @NotNull Object deserializeObject(@NotNull Object o);
    @Nullable Object deserializeObjectRaw(@NotNull Type type, @NotNull FileElement from, @NotNull FileContext<?> context);

    Supplier<FileContext<?>> contextSupplier();
    void contextSupplier(Supplier<FileContext<?>> suppler);

    @NotNull
    FileElement serializeToFile(@Nullable Object object);
    @NotNull
    FileElement serializeToFile(@Nullable Object object, @NotNull FileContext<?> context);
    <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o);
    <T> @Nullable T deserializeFromFile(@NotNull Type type, @Nullable FileElement o, @NotNull FileContext<?> context);
    <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o);
    <T> @Nullable T deserializeFromFile(@NotNull Class<T> clazz, @Nullable FileElement o, @NotNull FileContext<?> context);

    default <T> T deserializeFromFileOrDefault(@NotNull Type type, @Nullable FileElement o, T fallback){
        T got = deserializeFromFile(type, o);
        return got == null ? fallback : got;
    }
    default <T> T deserializeFromFileOrDefault(@NotNull Type type, @Nullable FileElement o, @NotNull FileContext<?> context, T fallback){
        T got = deserializeFromFile(type, o, context);
        return got == null ? fallback : got;
    }
    default <T> T deserializeFromFileOrDefault(@NotNull Class<T> clazz, @Nullable FileElement o, T fallback){
        T got = deserializeFromFile(clazz, o);
        return got == null ? fallback : got;
    }
    default <T> T deserializeFromFileOrDefault(@NotNull Class<T> clazz, @Nullable FileElement o, @NotNull FileContext<?> context, T fallback){
        T got = deserializeFromFile(clazz, o, context);
        return got == null ? fallback : got;
    }

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
