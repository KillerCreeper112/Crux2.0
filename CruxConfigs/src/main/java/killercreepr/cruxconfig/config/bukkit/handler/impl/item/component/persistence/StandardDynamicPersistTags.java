package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import killercreepr.crux.Crux;
import killercreepr.crux.item.dynamic.components.persistence.DynamicPersistentTag;
import killercreepr.crux.item.dynamic.components.persistence.TypedDynamicPersistentTag;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StandardDynamicPersistTags {
    public static void register(KeyedRegistry<FileDynamicPersistTagParser<?>> registry){
        registry.register(new FilePersistentContainerDynamicPersistTag(Crux.key("tag_container")));
        registry.register(new FileListDynamicPersistTag(Crux.key("list")));
        registry.register(new BaseSimplePersistentParser<>(Crux.key("string")) {

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TypedDynamicPersistentTag object) {
                return null;
            }

            @Override
            public @Nullable String parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
                return e.getAsString();
            }

            @Override
            public @NotNull DynamicPersistentTag getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                return DynamicPersistentTag.STRING;
            }
        });
    }
}
