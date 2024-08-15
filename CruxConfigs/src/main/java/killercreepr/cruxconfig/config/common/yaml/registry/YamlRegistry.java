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
    default void registerYamlHandler(@NotNull AutoYamlSerializer<?>... serializers){
        for(AutoYamlSerializer<?> d : serializers){
            registerYamlHandler(d.getType(), d);
        }
    }
    @NotNull
    YamlElement serializeToYaml(@NotNull Object object);
    <T> @Nullable T deserializeFromYaml(@NotNull Type type, @Nullable YamlElement o);
    <T> @Nullable T deserializeFromYaml(@NotNull Class<T> clazz, @Nullable YamlElement o);
    <T> @Nullable T deserializeFromYaml(@NotNull Class<T> clazz, @Nullable YamlElement o, @NotNull YamlContext context);
    @Nullable Object deserializeObjectFromYaml(@NotNull YamlElement o);
    <T extends YamlObjectHandler<?>> void registerYamlHandler(@NotNull Class<?> clazz, @NotNull T handler);
}
