package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

public class YamlMenuItem implements YamlObjectHandler<MenuItemHolder> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull MenuItemHolder object) {
        throw new UnsupportedOperationException("MenuItem may not be serialized!");
    }

    @Override
    public @Nullable MenuItemHolder deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        YamlRegistry registry = context.getRegistry();
        MenuItemHolder base = null;
        /*todo if(menuConfig != null){
            String baseID = cfg.getString(addDot(path) + "base");
            if(baseID != null){
                String[] checkBase = path.split("\\.");
                if(checkBase.length > 0 && checkBase[checkBase.length-1].contains(baseID)){
                    Crux.log(Level.WARNING, "Menu item has the same base ID as its own ID! (" + path + ") Base = (" + baseID + "). Ignoring...");
                }else base = new MenuItemValue().get(menuConfig, "items." + baseID, menuConfig);
            }
        }*/

        DataExchange.Builder extraInfo = new DataExchange.Builder();
        if(base != null) extraInfo.putAll(base.info());//addDot(path) + "extra"
        extraInfo.putAll((DataExchange) registry.deserializeObject(DataExchange.class, o.get("data")));
        //todo? extraInfo.putAll(new DataExchangeValue().get(config, removeDot(path)));
        //extraInfo.putAll(new DataExchangeValue().get(config, addDot(path) + "data"));
        //addCustomInfo(item);

        ItemStack i;
        if(base == null) i = new ItemStackValue().get(config, addDot(path) + "item");
        else{
            ItemStack baseClone = base.getItem().value();
            if(baseClone != null) baseClone = baseClone.clone();
            i = new ItemStackValue().get(config, addDot(path) + "item", baseClone);
        }

        YamlObject display;
        if(o.get("display") instanceof YamlObject oo) display = oo;
        else display = null;

        MenuItemHolder item = new MenuItemHolder(
                Holder.direct(i),
                extraInfo.build(),
                display == null ? null : display.getObject(String.class, "name"),
                display == null ? null : display.getObject(List.class, "lore"),

        );

        MenuItemHolder item = new MenuItemHolder(Holder.direct(i),
                extraInfo.build(),
                cfg.getString(addDot(path) + "display.name", base == null ? null : base.getDisplayName()),
                cfg.isList(addDot(path) + "display.lore") ?
                        cfg.getStringList(addDot(path) + "display.lore") :
                        base == null ? null : base.getDisplayLore(), new MenuActionsValue().get(config, addDot(path) + "actions", base));
        return item;
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
}
