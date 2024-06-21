package killercreepr.cruxmenu.menu.bukkit.holder;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.cruxmenu.menu.bukkit.MenuContext;
import killercreepr.cruxmenu.menu.bukkit.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuItemHolder {
    protected final @NotNull Holder<DynamicItem> item;
    protected final @NotNull DataExchange info;
    protected final @Nullable ClickActions clickActions;

    public MenuItemHolder(@NotNull Holder<DynamicItem> item, @NotNull DataExchange info,
                          @Nullable ClickActions clickActions) {
        this.item = item;
        this.info = info;
        this.clickActions = clickActions;
    }

    public @NotNull DataExchange info() {
        return info;
    }

    public @NotNull MenuItem getDisplayItem(@NotNull Player p, @NotNull MenuContext info) {
        Evaluation evaluation = new Evaluation(this, p, info);
        return new MenuItem(this, evaluation.evaluateInfo());
    }

    public @NotNull Holder<DynamicItem> getItem() {
        return item;
    }

    public @Nullable ClickActions getClickActions() {
        return clickActions;
    }

    public abstract static class DisplayConsumer{
        public abstract @NotNull ItemStack accept(@NotNull Player viewer, @NotNull MenuItemHolder menuItem, @NotNull ItemStack item, @NotNull MergedTagContainer resolvers);
    }

    public static class Evaluation{
        private final @NotNull MenuItemHolder item;
        private final @NotNull Player p;
        private final @NotNull MenuContext info;

        public Evaluation(@NotNull MenuItemHolder item, @NotNull Player p, @NotNull MenuContext info) {
            this.item = item;
            this.p = p;
            this.info = info;
        }

        public @NotNull MenuContext evaluateInfo(){
            DataExchange.Builder extraInfo = new DataExchange.Builder();
            MergedTagContainer resolvers = new MultiTagContainer(this.info.getResolvers().getTagParser());
            return new MenuContext(this.info.getMenu(), extraInfo.build(), resolvers);
        }
    }
}
