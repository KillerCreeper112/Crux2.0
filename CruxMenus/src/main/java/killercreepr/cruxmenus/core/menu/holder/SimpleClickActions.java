package killercreepr.cruxmenus.core.menu.holder;

import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SimpleClickActions implements ClickActions {
    protected final @NotNull Map<ClickType, List<String>> actions;

    public SimpleClickActions(@NotNull Map<ClickType, List<String>> actions) {
        this.actions = actions;
    }

    public @NotNull Map<ClickType, List<String>> getActions() {
        return actions;
    }

    public @Nullable List<String> get(@Nullable ClickType click){
        return actions.get(click);
    }

    public List<String> getOrDefault(@Nullable ClickType click, @Nullable List<String> defaultValue){
        return actions.getOrDefault(click, defaultValue);
    }

    public List<String> clickOrDefault(@NotNull InventoryClickEvent event, @Nullable List<String> defaultValue){
        return getOrDefault(event.getClick(), getOrDefault(null, defaultValue));
    }
}
