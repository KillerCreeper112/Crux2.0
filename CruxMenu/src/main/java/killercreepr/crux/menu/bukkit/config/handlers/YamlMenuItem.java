package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.item.DynamicItem;
import killercreepr.crux.menu.bukkit.holder.ClickActions;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if(base != null) extraInfo.putAll(base.info());
        extraInfo.putAll(registry.deserialize(DataExchange.class, o.get("data")));
        extraInfo.putAll(registry.deserialize(DataExchange.class, o));

        DynamicItem i;
        if(base == null) i = registry.deserialize(DynamicItem.class, o.get("item"));
        else{
            DynamicItem baseClone = base.getItem().value();
            if(baseClone != null) baseClone = baseClone.clone();
            i = menuModule.getYamlDynamicItem().deserializeFromYaml(context, o.get("item"), baseClone);
        }

        ClickActions clickActions = menuModule.getYamlMenuActions().deserializeFromYaml(
                context, o.get("actions"), base
        );

        MenuItemHolder item = new MenuItemHolder(
                Holder.direct(i),
                extraInfo.build(),
                clickActions
        );
        return item;
    }
}
