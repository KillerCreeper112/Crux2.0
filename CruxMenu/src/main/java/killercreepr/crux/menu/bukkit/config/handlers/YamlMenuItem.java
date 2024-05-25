package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class YamlMenuItem extends YamlModuled<MenuItemHolder> {
    public YamlMenuItem(@NotNull YamlMenuModule menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable MenuItemHolder deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable YamlObject menuContext) {
        if(!(e instanceof YamlObject o)) return null;
        YamlRegistry registry = context.getRegistry();
        MenuItemHolder base = null;
        if(menuContext != null){
            String baseID = menuContext.getObject(String.class, "base");
            if(baseID != null){
                /*String[] checkBase = path.split("\\.");
                if(checkBase.length > 0 && checkBase[checkBase.length-1].contains(baseID)){
                    Crux.log(Level.WARNING, "Menu item has the same base ID as its own ID! (" + path + ") Base = (" + baseID + "). Ignoring...");
                }else{
                    base = menuModule.getYamlMenuItem().deserializeFromYaml(
                            context, o.get(baseID), menuContext
                    );
                }*/
                base = menuModule.getYamlMenuItem().deserializeFromYaml(
                        context, o.get(baseID), menuContext
                );
            }
        }

        DataExchange.Builder extraInfo = new DataExchange.Builder();
        if(base != null) extraInfo.putAll(base.info());//addDot(path) + "extra"
        extraInfo.putAll(registry.deserialize(DataExchange.class, o.get("data")));
        //todo? extraInfo.putAll(new DataExchangeValue().get(config, removeDot(path)));
        //extraInfo.putAll(new DataExchangeValue().get(config, addDot(path) + "data"));
        //addCustomInfo(item);

        ItemStack i;
        if(base == null) i = registry.deserialize(ItemStack.class, o.get("item"));
        else{
            ItemStack baseClone = base.getItem().value();
            if(baseClone != null) baseClone = baseClone.clone();
            i = menuModule.getYamlItemStack().deserializeFromYaml(context, o.get("item"), baseClone);
        }

        YamlObject display;
        if(o.get("display") instanceof YamlObject oo) display = oo;
        else display = null;

        Map<ClickType, Collection<String>> clickActions = menuModule.getYamlMenuActions().deserializeFromYaml(
                context, o.get("actions"), base
        );

        MenuItemHolder item = new MenuItemHolder(
                Holder.direct(i),
                extraInfo.build(),
                display == null ? null : display.getObject(String.class, "name", base==null?null:base.getDisplayName()),
                display == null ? null : display.getObject(List.class, "lore",base==null?null:base.getDisplayLore()),
                clickActions
        );

        /*MenuItemHolder item = new MenuItemHolder(Holder.direct(i),
                extraInfo.build(),
                cfg.getString(addDot(path) + "display.name", base == null ? null : base.getDisplayName()),
                cfg.isList(addDot(path) + "display.lore") ?
                        cfg.getStringList(addDot(path) + "display.lore") :
                        base == null ? null : base.getDisplayLore(), new MenuActionsValue().get(config, addDot(path) + "actions", base));*/
        return item;
    }
}
