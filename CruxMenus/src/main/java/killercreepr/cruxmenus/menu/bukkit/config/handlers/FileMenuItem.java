package killercreepr.cruxmenus.menu.bukkit.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.holder.ClickActions;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileMenuItem extends FileModuled<MenuItemHolder> {
    public FileMenuItem(@NotNull FileMenuModule menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable MenuItemHolder deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
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
                base = menuModule.getYamlMenuItem().deserializeFromFile(
                    context, o.get(baseID), menuContext
                );
            }
        }

        DataExchange.Builder extraInfo = new DataExchange.Builder();
        if(base != null) extraInfo.putAll(base.info());
        extraInfo.putAll(registry.deserialize(DataExchange.class, o));
        extraInfo.putAll(registry.deserialize(DataExchange.class, o.get("data")));

        DynamicItem i;
        if(base == null) i = registry.deserialize(DynamicItem.class, o.get("item"));
        else{
            DynamicItem baseClone = base.getItem().value();
            if(baseClone != null) baseClone = baseClone.clone();
            i = menuModule.getYamlDynamicItem().deserializeFromYaml(context, o.get("item"), baseClone);
        }

        ClickActions clickActions = menuModule.getYamlMenuActions().deserializeFromFile(
            context, o.get("actions"), base
        );

        MenuItemHolder item = new MenuItemHolder(
            Holder.direct(i),
            extraInfo.build(),
            clickActions
        );
        return item;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_item";
    }
}
