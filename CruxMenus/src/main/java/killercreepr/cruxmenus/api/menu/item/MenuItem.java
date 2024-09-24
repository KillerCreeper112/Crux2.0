package killercreepr.cruxmenus.api.menu.item;

import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.format.FormatSerializer;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxmenus.api.event.MenuItemClickEvent;
import killercreepr.cruxmenus.api.menu.action.MenuAction;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.core.menu.item.SimpleMenuItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface MenuItem {
    static MenuItem item(@NotNull MenuItemHolder base, @NotNull MenuContext inputtedContext, @NotNull MenuContext evaluatedContext){
        return new SimpleMenuItem(base, inputtedContext, evaluatedContext);
    }
    @NotNull FormatSerializer getFormat();

    @NotNull Optional<Integer> getSlot();

    boolean canDisplay();

    @NotNull String setPlaceholders(@NotNull String text);

    @NotNull MergedTagContainer buildTags();

    @Nullable ItemStack buildItem(@NotNull Player p);

    @NotNull CompletableFuture<CruxItem> buildItemCompletely(@NotNull Player p);

    @NotNull MenuItemClickEvent click(@NotNull Player p, @NotNull InventoryClickEvent event);

    boolean performAction(@NotNull Player p, @NotNull String action, @NotNull ActionContext actionInfo);

    boolean performAction(@NotNull Player p, @NotNull String action,
                          @NotNull ActionContext actionInfo, @NotNull Registry<MenuAction> actions);

    @NotNull
    MenuItemHolder getBase();


    @NotNull
    MenuContext getInputtedContext();

    @NotNull
    MenuContext getEvaluatedContext();
}
