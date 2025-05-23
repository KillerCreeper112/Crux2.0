package killercreepr.cruxmenus.api.menu.action.click;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface ClickActions {
    @Nullable List<String> get(@Nullable ClickType click);

    List<String> getOrDefault(@Nullable ClickType click, @Nullable List<String> defaultValue);

    List<String> clickOrDefault(@NotNull InventoryClickEvent event, @Nullable List<String> defaultValue);
    Map<ClickType, List<String>> getActions();
}
