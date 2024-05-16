package killercreepr.crux.menu.bukkit.holder;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.MenuInfo;
import killercreepr.crux.menu.bukkit.MenuItem;
import killercreepr.crux.tags.container.ObjectStringHookContainer;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.crux.tags.defaults.CClaimTags;
import killercreepr.crux.tags.format.FormatPrefix;
import killercreepr.crux.util.CruxMath;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MenuItemHolder {
    protected final @NotNull Holder<ItemStack> item;
    protected final @NotNull DataExchange info;
    protected final @Nullable String displayName;
    protected final @Nullable List<String> displayLore;
    protected final @Nullable Map<ClickType, Collection<String>> clickActions;

    public MenuItemHolder(@NotNull Holder<ItemStack> item, @NotNull DataExchange info,
                          @Nullable String displayName, @Nullable List<String> displayLore,
                          @Nullable Map<ClickType, Collection<String>> clickActions) {
        this.item = item;
        this.info = info;
        this.displayName = displayName;
        this.displayLore = displayLore;
        this.clickActions = clickActions;
    }

    public @NotNull DataExchange info() {
        return info;
    }

    public @NotNull MenuItem getDisplayItem(@NotNull Player p, @NotNull MenuInfo info) {
        Evaluation evaluation = new Evaluation(this, p, info);
        return new MenuItem(this, evaluation.evaluateInfo());
    }

    public @NotNull Holder<ItemStack> getItem() {
        return item;
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }

    public @Nullable List<String> getDisplayLore() {
        return displayLore;
    }

    public @Nullable Map<ClickType, Collection<String>> getClickActions() {
        return clickActions;
    }

    public abstract static class DisplayConsumer{
        public abstract @NotNull ItemStack accept(@NotNull Player viewer, @NotNull MenuItemHolder menuItem, @NotNull ItemStack item, @NotNull StringHookContainer resolvers);
    }

    public static class Evaluation{
        private final @NotNull MenuItemHolder item;
        private final @NotNull Player p;
        private final @NotNull MenuInfo info;

        public Evaluation(@NotNull MenuItemHolder item, @NotNull Player p, @NotNull MenuInfo info) {
            this.item = item;
            this.p = p;
            this.info = info;
        }

        //todo remofe test;
        private final List<CClaimTags.TestBois> ayo = new ArrayList<>(){{
            add(new CClaimTags.TestBois("1 boi"));
            add(new CClaimTags.TestBois("2 boi"));
            add(new CClaimTags.TestBois("3 boi"));
            add(new CClaimTags.TestBois("4 boi"));
        }};
        public @NotNull MenuInfo evaluateInfo(){
            DataExchange info = this.info.getInfo().append(item.info());
            DataExchange.Builder extraInfo = new DataExchange.Builder();
            ObjectStringHookContainer resolvers = new ObjectStringHookContainer(this.info.getResolvers());

            info.getObject("outpost_id", String.class).ifPresent(eq ->{
                int outpostID = (int) CruxMath.evaluate(Crux.FORMAT.deserializeString(eq, resolvers));
                extraInfo.put("outpost", Holder.direct(ayo.get(outpostID)));
            });

            /*info.get("outpost_id", String.class).ifPresent(eq ->{
                int outpostID = (int) CruxMath.evaluate(CClaim.FORMAT.deserializeString(eq, resolvers));
                Outpost post = CClaim.inst().getPlayerManager().getClaimPlayer(p).getOutposts().getOrDefault(outpostID, null);
                extraInfo.set("outpost", post);
            });*/
            resolvers.setPrefixBuilder("member",(data, id, object) -> FormatPrefix.addonPlusHook(""));
            return new MenuInfo(this.info.getMenu(), extraInfo.build(), resolvers);
        }
    }
}
