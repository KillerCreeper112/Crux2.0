package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.yaml.automatic.AutoYamlSerializer;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface YamlRegistry extends FileRegistry {
    default void registerHandler(@NotNull AutoYamlSerializer<?>... serializers){
        for(AutoYamlSerializer<?> d : serializers){
            registerHandler(d.getType(), d);
        }
    }
    @NotNull
    YamlElement serializeObject(@NotNull Object object);
    @NotNull
    YamlElement serializeToYamlElement(@NotNull Object object);
    <T> @Nullable T deserialize(@NotNull Type type, @Nullable YamlElement o);
    <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable YamlElement o);
    <T> @Nullable T deserialize(@NotNull Class<T> clazz, @Nullable YamlElement o, @NotNull YamlContext context);
    @Nullable Object deserializeObject(@NotNull YamlElement o);
    <T extends YamlObjectHandler<?>> void registerHandler(@NotNull Class<?> clazz, @NotNull T handler);
}
