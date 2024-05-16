package killercreepr.crux.config.bukkit.data;

import killercreepr.crux.config.bukkit.CruxConfig;
import killercreepr.crux.config.bukkit.value.ConfigValue;
import killercreepr.crux.menu.holder.MenuItemHolder;
import killercreepr.crux.menu.holder.MenuItems;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class MenuItemsValue extends ConfigValue<MenuItems> {
    public MenuItemsValue(@Nullable MenuItems defaultValue) {
        super(MenuItems.class, defaultValue);
    }

    public MenuItemsValue() {
        this(null);
    }

    @Override
    public @Nullable MenuItems get(@NotNull CruxConfig cfg, @NotNull String path) {
        return null;
    }

    public @NotNull MenuItems get(@NotNull CruxConfig crux, @NotNull String path, @Nullable CruxConfig menuConfig){
        FileConfiguration cfg = crux.config();
        MenuItems map = new MenuItems(new TreeMap<>());
        ConfigurationSection section = cfg.getConfigurationSection(removeDot(path));
        if(section == null) return map;
        for(String s : section.getKeys(false)){
            MenuItemHolder item = new MenuItemValue().get(crux, addDot(path) + s + ".", menuConfig);
            if(item != null) map.add(item);
        }
        return map;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable MenuItems object) {

    }
}
