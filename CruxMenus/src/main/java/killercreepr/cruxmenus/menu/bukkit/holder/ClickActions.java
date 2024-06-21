package killercreepr.cruxmenus.menu.bukkit.holder;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class ClickActions {
    protected final @NotNull Map<ClickType, Collection<String>> actions;

    public ClickActions(@NotNull Map<ClickType, Collection<String>> actions) {
        this.actions = actions;
    }

    public @NotNull Map<ClickType, Collection<String>> getActions() {
        return actions;
    }

    public @Nullable Collection<String> get(@NotNull ClickType click){
        return actions.get(click);
    }

    public Collection<String> getOrDefault(@NotNull ClickType click, @Nullable Collection<String> defaultValue){
        return actions.getOrDefault(click, defaultValue);
    }
}
