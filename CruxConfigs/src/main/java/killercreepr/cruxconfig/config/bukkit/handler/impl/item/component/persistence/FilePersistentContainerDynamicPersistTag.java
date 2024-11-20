package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import killercreepr.crux.api.item.dynamic.component.persistence.DynamicPersistentTag;
import killercreepr.crux.api.item.dynamic.component.persistence.TypedDynamicPersistentTag;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FilePersistentContainerDynamicPersistTag extends BaseSimplePersistentParser<List<TypedDynamicPersistentTag<?>>>{
    public FilePersistentContainerDynamicPersistTag(@NotNull Key key) {
        super(key);
    }

    @Override
    public @Nullable FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object) {
        return null;//todo
    }

    @Override
    public @Nullable List<TypedDynamicPersistentTag<?>> parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
        if(!(e instanceof FileArray a)) return null;
        List<TypedDynamicPersistentTag<?>> list = new ArrayList<>();
        a.forEach(ele ->{
            TypedDynamicPersistentTag<?> tag = ctx.getRegistry().deserializeFromFile(TypedDynamicPersistentTag.class, ele);
            if(tag==null) return;
            list.add(tag);
        });
        return list;
    }

    @Override
    public @NotNull DynamicPersistentTag<List<TypedDynamicPersistentTag<?>>, PersistentDataContainer> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        return DynamicPersistentTag.TAG_CONTAINER;
    }
}
