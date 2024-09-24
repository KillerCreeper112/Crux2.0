package killercreepr.cruxmenus.api.menu.action.click;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface ClickActions {
    @Nullable Collection<String> get(@Nullable ClickType click);

    Collection<String> getOrDefault(@Nullable ClickType click, @Nullable Collection<String> defaultValue);

    Collection<String> clickOrDefault(@NotNull InventoryClickEvent event, @Nullable Collection<String> defaultValue);
    Map<ClickType, Collection<String>> getActions();
}
