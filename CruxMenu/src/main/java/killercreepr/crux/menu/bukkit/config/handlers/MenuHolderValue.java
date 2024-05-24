package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.Crux;
import killercreepr.crux.config.bukkit.CruxConfig;
import killercreepr.crux.config.bukkit.value.ConfigValue;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.menu.bukkit.holder.MenuHolder;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuHolderValue extends ConfigValue<MenuHolder> {
    public MenuHolderValue(@Nullable MenuHolder defaultValue) {
        super(MenuHolder.class, defaultValue);
    }

    public MenuHolderValue() {
        this(null);
    }

    @Override
    public @Nullable MenuHolder get(@NotNull CruxConfig cfg, @NotNull String path) {
        String id = cfg.file().getName().replace(".yml", "");
        String titleString = cfg.config().getString(addDot(path) + "title");
        String title = titleString == null ? "" : titleString;
        String sizeString = cfg.config().getString(addDot(path) + "size");
        String size = sizeString == null ? "" : sizeString;
        DataExchange extraInfo = new DataExchangeValue().get(cfg, addDot(path) + "data");

        String potentialKey = cfg.config().getString(addDot(path) + "key");
        NamespacedKey key = Crux.key(potentialKey == null ? id : potentialKey);
        MenuHolder holder = new MenuHolder(key,title, size,
                new MenuItemsValue().get(cfg, addDot(path) + "items", cfg), extraInfo == null ? DataExchange.empty(): extraInfo);
        return holder;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable MenuHolder object) {
        //not used
    }
}
