package killercreepr.crux.menu.bukkit.config.handlers.old;

import killercreepr.crux.Crux;
import killercreepr.crux.config.bukkit.CruxConfig;
import killercreepr.crux.config.bukkit.value.ConfigValue;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class MenuItemValue extends ConfigValue<MenuItemHolder> {
    public MenuItemValue(@Nullable MenuItemHolder defaultValue) {
        super(MenuItemHolder.class, defaultValue);
    }

    public MenuItemValue() {
        this(null);
    }

    @Override
    public @Nullable MenuItemHolder get(@NotNull CruxConfig cfg, @NotNull String path) {
        return get(cfg, path, null);
    }

    public @Nullable MenuItemHolder get(@NotNull CruxConfig config, @NotNull String path, @Nullable CruxConfig menuConfig){
        FileConfiguration cfg = config.config();
        MenuItemHolder base = null;
        if(menuConfig != null){
            String baseID = cfg.getString(addDot(path) + "base");
            if(baseID != null){
                String[] checkBase = path.split("\\.");
                if(checkBase.length > 0 && checkBase[checkBase.length-1].contains(baseID)){
                    Crux.log(Level.WARNING, "Menu item has the same base ID as its own ID! (" + path + ") Base = (" + baseID + "). Ignoring...");
                }else base = new MenuItemValue().get(menuConfig, "items." + baseID, menuConfig);
            }
        }

        DataExchange.Builder extraInfo = new DataExchange.Builder();
        if(base != null) extraInfo.putAll(base.info());//addDot(path) + "extra"
        extraInfo.putAll(new DataExchangeValue().get(config, removeDot(path)));
        extraInfo.putAll(new DataExchangeValue().get(config, addDot(path) + "data"));
        //addCustomInfo(item);

        ItemStack i;
        if(base == null) i = new ItemStackValue().get(config, addDot(path) + "item");
        else{
            ItemStack baseClone = base.getItem().value();
            if(baseClone != null) baseClone = baseClone.clone();
            i = new ItemStackValue().get(config, addDot(path) + "item", baseClone);
        }

        MenuItemHolder item = new MenuItemHolder(Holder.direct(i),
                extraInfo.build(),
                cfg.getString(addDot(path) + "display.name", base == null ? null : base.getDisplayName()),
                cfg.isList(addDot(path) + "display.lore") ?
                        cfg.getStringList(addDot(path) + "display.lore") :
                        base == null ? null : base.getDisplayLore(), new MenuActionsValue().get(config, addDot(path) + "actions", base));
        return item;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable MenuItemHolder object) {
        //not used
    }
}
