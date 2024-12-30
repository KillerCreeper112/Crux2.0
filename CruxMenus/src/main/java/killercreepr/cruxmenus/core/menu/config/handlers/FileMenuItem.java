package killercreepr.cruxmenus.core.menu.config.handlers;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class FileMenuItem extends SimpleFileMenuModuled<MenuItemHolder> {
    public FileMenuItem(@NotNull FileMenuHolder<?> menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable MenuItemHolder deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return deserializeFromFile(context, e, menuContext, null);
    }

    public @Nullable MenuItemHolder deserializeFromFile(@NotNull FileContext<?> context,
                                                        @NotNull FileElement e,
                                                        @Nullable FileObject menuContext,
                                                        @Nullable List<MenuItemHolder> defaultBase) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        List<MenuItemHolder> base = new ArrayList<>();
        if(defaultBase != null) base.addAll(defaultBase);
        if(menuContext != null) {
            List<String> baseIDs = new ArrayList<>();
            String baseID = o.getObject(String.class, "base");
            if (baseID != null) {
                baseIDs.add(baseID);
            }else{
                baseIDs = registry.deserializeFromFile(new TypeToken<List<String>>(){}.getType(), o.get("base"));
                if(baseIDs == null) baseIDs = List.of();
            }
            for(String s : baseIDs){
                MenuItemHolder possibleBase = menuModule.getFileMenuItem().deserializeFromFile(
                    context, menuContext.get("items").getAsFileObject().get(s), menuContext
                );
                if (possibleBase != null){
                    base.add(possibleBase);
                }else{
                    Crux.log(Level.WARNING, "No item base of " + s + " found!");
                }
            }
        }

        DataExchange.Builder extraInfo = DataExchange.builder();
        if(!base.isEmpty()){
            base.forEach(b -> extraInfo.putAll(b.info()));
        }
        extraInfo.putAll(registry.deserializeFromFile(DataExchange.class, o));
        extraInfo.putAll(registry.deserializeFromFile(DataExchange.class, o.get("data")));

        DynamicItem i;
        if(base.isEmpty()) i = registry.deserializeFromFile(DynamicItem.class, o.get("item"));
        else{
            DynamicItem baseClone = base.getLast().getItem().value();
            if(baseClone != null) baseClone = baseClone.clone();
            i = menuModule.getFileDynamicItem().deserializeFromYaml(context, o.get("item"), baseClone);
            int size = base.size()-2;
            while(size >= 0){
                if(i == null) break;
                baseClone = base.get(size).getItem().value();
                if(baseClone != null) baseClone = baseClone.clone();
                if(baseClone != null) i = i.mergeItem(baseClone);
                size--;
            }
        }
        ClickActions clickActions = menuModule.getFileMenuActions().deserializeFromFile(
            context, o.get("actions"), base
        );

        //if(clickActions == null && !base.isEmpty()) clickActions = base.getClickActions();

        return MenuItemHolder.holder(
            Holder.direct(i),
            extraInfo.build(),
            clickActions
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_item";
    }
}
