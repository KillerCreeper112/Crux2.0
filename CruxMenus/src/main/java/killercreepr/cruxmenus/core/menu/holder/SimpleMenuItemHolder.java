package killercreepr.cruxmenus.core.menu.holder;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.text.container.SimpleMergedTagContainer;
import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleMenuItemHolder implements MenuItemHolder {
    protected final @NotNull Holder<DynamicItem> item;
    protected final @NotNull DataExchange info;
    protected final @Nullable ClickActions clickActions;

    public SimpleMenuItemHolder(@NotNull Holder<DynamicItem> item, @NotNull DataExchange info,
                                @Nullable ClickActions clickActions) {
        this.item = item;
        this.info = info;
        this.clickActions = clickActions;
    }

    public @NotNull DataExchange info() {
        return info;
    }

    public @NotNull MenuItem getDisplayItem(@NotNull Entity p, @NotNull MenuContext info) {
        Evaluation evaluation = new Evaluation(this, p, info);
        return MenuItem.item(this, info, evaluation.evaluateInfo());
    }

    public @NotNull Holder<DynamicItem> getItem() {
        return item;
    }

    public @Nullable ClickActions getClickActions() {
        return clickActions;
    }

    public static class Evaluation implements MenuItemHolder.InfoEvaluator{
        private final @NotNull MenuItemHolder item;
        private final @NotNull Entity p;
        private final @NotNull MenuContext context;

        public Evaluation(@NotNull MenuItemHolder item, @NotNull Entity p, @NotNull MenuContext context) {
            this.item = item;
            this.p = p;
            this.context = context;
        }

        public @NotNull MenuContext evaluateInfo(){
            DataExchange.Builder parsedInfo = DataExchange.builder();
            MergedTagContainer resolvers = new SimpleMergedTagContainer(this.context.getResolvers().getTagParser());
            context.getMenu().getHolder().getRegistry().itemDataParsers().forEachSorted(parser ->{
                parsedInfo.putAll(parser.parse(p, context, item));
            });
            DataExchange totalParsed = parsedInfo.build();
            resolvers.hookAll(totalParsed);
            return MenuContext.context(this.context.getMenu(), totalParsed, resolvers);
        }
    }
}
