package killercreepr.cruxmenus.core.menu.config.handlers;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.core.menu.holder.SimpleClickActions;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FileMenuActions extends SimpleFileMenuModuled<ClickActions> {

    public FileMenuActions(@NotNull FileMenuHolder<?> menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable ClickActions deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        throw new UnsupportedOperationException("MenuActions does not have a deserialize implementation!");
    }

    public @Nullable ClickActions deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable Collection<MenuItemHolder> base) {
        if (!(e instanceof FileObject o)){
            if(base != null) {
                Map<ClickType, Collection<String>> map = new HashMap<>();
                base.forEach(b ->{
                    ClickActions actions = b.getClickActions();
                    if(actions==null) return;
                    map.putAll(actions.getActions());
                });
                return new SimpleClickActions(map);
            }
            return null;
        }
        FileRegistry registry = context.getRegistry();

        Map<ClickType, Collection<String>> map = new HashMap<>();
        if(base != null) {
            base.forEach(b ->{
                ClickActions actions = b.getClickActions();
                if(actions==null) return;
                map.putAll(actions.getActions());
            });
        }
        Crux.log(Level.WARNING, "CLICK_TYPE=" + base);

        /*Map<ClickType, Collection<String>> map = base == null || base.getClickActions() == null ?
            new HashMap<>() : new HashMap<>(base.getClickActions().getActions());*/
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
            Collection<String> actions = registry.deserializeFromFile(
                new TypeToken<Collection<String>>(){}.getType(), o.get(key));
            if (actions != null) map.put(type, actions);
        });
        return map.isEmpty() ? null : new SimpleClickActions(map);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_actions";
    }
}
