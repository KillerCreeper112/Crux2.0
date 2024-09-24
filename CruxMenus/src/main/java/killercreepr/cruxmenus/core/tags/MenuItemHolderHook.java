package killercreepr.cruxmenus.core.tags;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuItemHolderHook implements ObjectTag<MenuItemHolder> {
    @Override
    public @NotNull Class<MenuItemHolder> getObjectType() {
        return MenuItemHolder.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("itemholder_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull MenuItemHolder object, @NotNull TagParser tags) {
        StringTagContainer c = new StringTagContainer(tags);
        object.info().asMap().forEach((id, holder) ->{
            Object o = holder.value();
            c.add(Tag.parsed(id, o==null?"null":o.toString()));
        });
        return c;
    }
}
