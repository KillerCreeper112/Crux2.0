package killercreepr.cruxmenus.api.menu.item;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.api.event.MenuItemClickEvent;
import killercreepr.cruxmenus.api.menu.action.MenuAction;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.core.menu.item.SimpleMenuItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface MenuItem {
    static MenuItem item(@NotNull MenuItemHolder base, @NotNull MenuContext inputtedContext, @NotNull MenuContext evaluatedContext){
        return new SimpleMenuItem(base, inputtedContext, evaluatedContext);
    }
    @NotNull FormatSerializer getFormat();

    @NotNull Optional<List<Number>> getSlots();

    boolean canDisplay();

    @NotNull String setPlaceholders(@NotNull String text);

    @NotNull MergedTagContainer buildTags();

    @Nullable ItemStack buildItem(@NotNull HumanEntity p);

    @NotNull CompletableFuture<CruxItem> buildItemCompletely(@NotNull HumanEntity p);

    @NotNull MenuItemClickEvent click(@NotNull HumanEntity p, @NotNull InventoryClickEvent event);

    boolean performAction(@NotNull HumanEntity p, @NotNull String action, @NotNull ActionContext actionInfo);

    boolean performAction(@NotNull HumanEntity p, @NotNull String action,
                          @NotNull ActionContext actionInfo, @NotNull Registry<MenuAction> actions);

    @NotNull
    MenuItemHolder getBase();


    @NotNull
    MenuContext getInputtedContext();

    @NotNull
    MenuContext getEvaluatedContext();
}
