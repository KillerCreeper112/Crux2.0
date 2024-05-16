package killercreepr.crux.config.bukkit.data;

import killercreepr.crux.Crux;
import killercreepr.crux.config.bukkit.CruxConfig;
import killercreepr.crux.config.bukkit.value.ConfigValue;
import killercreepr.crux.menu.holder.MenuItemHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MenuActionsValue extends ConfigValue<Map<ClickType, Collection<String>>> {
    public MenuActionsValue(@Nullable Map<ClickType, Collection<String>> defaultValue) {
        super((Class<Map<ClickType, Collection<String>>>) (Class<?>) Map.class, defaultValue);
    }

    public MenuActionsValue() {
        this(null);
    }

    @Override
    public @Nullable Map<ClickType, Collection<String>> get(@NotNull CruxConfig cfg, @NotNull String path) {
        return get(cfg, path, null);
    }

    public @Nullable Map<ClickType, Collection<String>> get(@NotNull CruxConfig config, @NotNull String path, @Nullable MenuItemHolder base){
        FileConfiguration cfg = config.config();
        Map<ClickType, Collection<String>> map = base == null || base.getClickActions() == null ?
                new HashMap<>() : new HashMap<>(base.getClickActions());
        ConfigurationSection section = cfg.getConfigurationSection(removeDot(path));
        if(section == null) return null;
        for(String s : section.getKeys(false)){
            ClickType type;
            try{ type = ClickType.valueOf(s.toUpperCase()); }
            catch (IllegalArgumentException ignored){
                Crux.log(Level.WARNING, "Click type of '" + s + "' not found!");
                continue;
            }
            Collection<String> actions = cfg.getStringList(addDot(path) + s);
            if(!actions.isEmpty()) map.put(type, actions);
        }
        return map.isEmpty() ? null : map;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Map<ClickType, Collection<String>> object) {
        cfg.set(removeDot(path),null);
        if(object == null) return;
        object.forEach((key, value) -> cfg.set(addDot(path) + key.toString().toLowerCase(), value));
    }
}
