package killercreepr.cruxadvancements.core.config.handler;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxadvancements.core.advancement.icon.AdvancementItemIcon;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileAdvancementItemIcon implements FileObjectHandler<AdvancementItemIcon> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull AdvancementItemIcon object) {
        /*FileRegistry registry = ctx.getRegistry();
        FileObject o = new FileObject();
        o.add("key", registry.serializeToFile(object.key()));
        if(object.parent() != null) o.add("parent", registry.serializeToFile(object.parent()));
        o.add("criteria", registry.serializeToFile(object.getCriteria()));
        o.add("display", registry.serializeToFile(object.getDisplay()));
        o.add("flags", registry.serializeToFile(object.getFlags()));
        return o;*/
        return null;
    }

    @Override
    public @Nullable AdvancementItemIcon deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        return new AdvancementItemIcon(
            registry.deserializeFromFile(DynamicItem.class, e).buildItem(TextParserContext.empty())
        );
    }
}
