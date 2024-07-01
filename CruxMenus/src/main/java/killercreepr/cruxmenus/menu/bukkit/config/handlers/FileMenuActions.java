package killercreepr.cruxmenus.menu.bukkit.config.handlers;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.holder.ClickActions;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FileMenuActions extends FileModuled<ClickActions> {

    public FileMenuActions(@NotNull FileMenuModule menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable ClickActions deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        throw new UnsupportedOperationException("MenuActions does not have a deserialize implementation!");
    }

    public @Nullable ClickActions deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable MenuItemHolder base) {
        if (!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();

        Map<ClickType, Collection<String>> map = base == null || base.getClickActions() == null ?
            new HashMap<>() : new HashMap<>(base.getClickActions().getActions());
        o.forEach((key, value) -> {
            ClickType type;
            if(!key.equalsIgnoreCase("any")){
                try {
                    type = ClickType.valueOf(key.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    Crux.log(Level.WARNING, "Click type of '" + key + "' not found!");
                    return;
                }
            }else type = null;
            Collection<String> actions = registry.deserialize((Class<Collection<String>>) (Class<?>) Collection.class, o.get(key));
            if (actions == null || !actions.isEmpty()) map.put(type, actions);
        });
        return map.isEmpty() ? null : new ClickActions(map);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_actions";
    }
}
