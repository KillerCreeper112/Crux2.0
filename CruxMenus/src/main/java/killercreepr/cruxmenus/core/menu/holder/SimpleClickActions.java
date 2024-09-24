package killercreepr.cruxmenus.core.menu.holder;

import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class SimpleClickActions implements ClickActions {
    protected final @NotNull Map<ClickType, Collection<String>> actions;

    public SimpleClickActions(@NotNull Map<ClickType, Collection<String>> actions) {
        this.actions = actions;
    }

    public @NotNull Map<ClickType, Collection<String>> getActions() {
        return actions;
    }

    public @Nullable Collection<String> get(@Nullable ClickType click){
        return actions.get(click);
    }

    public Collection<String> getOrDefault(@Nullable ClickType click, @Nullable Collection<String> defaultValue){
        return actions.getOrDefault(click, defaultValue);
    }

    public Collection<String> clickOrDefault(@NotNull InventoryClickEvent event, @Nullable Collection<String> defaultValue){
        return getOrDefault(event.getClick(), getOrDefault(null, defaultValue));
    }
}
