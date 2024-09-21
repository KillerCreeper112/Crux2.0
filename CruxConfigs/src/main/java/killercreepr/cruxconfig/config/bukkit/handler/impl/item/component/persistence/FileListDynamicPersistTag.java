package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence;

import killercreepr.crux.item.dynamic.components.persistence.DynamicPersistentTag;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FileListDynamicPersistTag extends BaseSimplePersistentParser<List<?>>{
    public FileListDynamicPersistTag(@NotNull Key key) {
        super(key);
    }

    @Override
    public @Nullable FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object) {
        return null;//todo
    }

    @Override
    public @Nullable List<?> parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
        if(!(e instanceof FileArray a)) return null;
        Key subType = ctx.getRegistry().deserializeFromFile(Key.class, base.get("sub_type"));
        if(subType == null) return null;
        FileDynamicPersistTagParser<?> parser = CfgRegistries.DYNAMIC_PERSIST_TAG_PARSER.get(subType);
        if(parser == null) throw new IllegalStateException("FileDynamicPersistTagParser of " + subType + " does not exist!");
        List<Object> list = new ArrayList<>();
        a.forEach(ele ->{
            Object o = parser.parseObject(ctx, base, ele);
            if(o != null) list.add(o);
        });
        return list;
    }

    @Override
    public @NotNull DynamicPersistentTag<List<?>, ?> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject base)) return null;
        Key subType = ctx.getRegistry().deserializeFromFile(Key.class, base.get("sub_type"));
        if(subType == null) return null;
        FileDynamicPersistTagParser<?> parser = CfgRegistries.DYNAMIC_PERSIST_TAG_PARSER.get(subType);
        return DynamicPersistentTag.list(parser.getDynamicTag(ctx, e));
    }
}
