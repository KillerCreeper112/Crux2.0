package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import killercreepr.crux.api.item.dynamic.component.persistence.DynamicPersistentTag;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class StandardDynamicPersistTags {
    public static void register(KeyedRegistry<FileDynamicPersistTagParser<?>> registry){
        registry.register(new FilePersistentContainerDynamicPersistTag(Crux.key("tag_container")));
        registry.register(new FileListDynamicPersistTag(Crux.key("list")));
        registry.register(new BaseSimplePersistentParser<>(Crux.key("string")) {
            @Override
            public @NotNull FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object) {
                return new FileGeneric(object.toString());
            }

            @Override
            public @Nullable String parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
                return e.getAsString();
            }

            @Override
            public @NotNull DynamicPersistentTag<Object, String> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                return DynamicPersistentTag.STRING;
            }
        });
        registry.register(new BaseSimplePersistentParser<>(Crux.key("boolean")) {
            @Override
            public @NotNull FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object) {
                return new FileGeneric(object.toString());
            }

            @Override
            public @Nullable String parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
                return e.getAsString();
            }

            @Override
            public @NotNull DynamicPersistentTag<Object, Boolean> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                return DynamicPersistentTag.BOOLEAN;
            }
        });
        registry.register(new BaseSimplePersistentParser<>(Crux.key("key")) {
            @Override
            public @NotNull FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object) {
                return new FileGeneric(object.toString());
            }

            @Override
            public @Nullable String parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
                return e.getAsString();
            }

            @Override
            public @NotNull DynamicPersistentTag<Object, Key> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                return DynamicPersistentTag.KEY;
            }
        });
        registry.register(number(Crux.key("integer"), DynamicPersistentTag.INTEGER, Number::intValue));
        registry.register(number(Crux.key("double"), DynamicPersistentTag.DOUBLE, Number::doubleValue));
        registry.register(number(Crux.key("short"), DynamicPersistentTag.SHORT, Number::shortValue));
        registry.register(number(Crux.key("float"), DynamicPersistentTag.FLOAT, Number::floatValue));
        registry.register(number(Crux.key("long"), DynamicPersistentTag.LONG, Number::longValue));
        registry.register(number(Crux.key("byte"), DynamicPersistentTag.BYTE, Number::longValue));
    }

    private static <T extends Number> FileDynamicPersistTagParser<Object> number(
        @NotNull Key key,
        @NotNull DynamicPersistentTag<Object, ?> persistentTag,
        @NotNull Function<Number, T> parser
        ){
        return new BaseSimplePersistentParser<>(key) {
            @Override
            public @NotNull FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object) {
                return new FileGeneric(object);
            }

            @Override
            public @Nullable T parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
                return parser.apply(e.getAsNumber());
            }

            @Override
            public @NotNull DynamicPersistentTag<Object, ?> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                return persistentTag;
            }
        };
    }
}
