package killercreepr.cruxmenus.core.menu.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.config.FileDataProvider;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.item.requirement.ViewCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FileDataExchange extends SimpleFileMenuModuled<DataExchange> {
    protected final MappedRegistry<String, FileDataProvider> DATA_TYPES = new SimpleMappedRegistry<>();
    public FileDataExchange(@NotNull FileMenuHolder<?> menuModule) {
        super(menuModule);
    }

    public MappedRegistry<String, FileDataProvider> getDataTypes() {
        return DATA_TYPES;
    }

    @Override
    public @Nullable DataExchange deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable FileObject menuContext) {
        if(!(e instanceof FileObject o)) return null;
        DataExchange.Builder builder = DataExchange.builder();
        o.forEach((s, element) ->{
            s = s.toLowerCase();
            switch (s){
                case "items" ->{
                    Map<String, MenuItemHolder> items = new HashMap<>();
                    if(!(element instanceof FileObject subSection)) return;
                    subSection.forEach((ss, subElement) ->{
                        MenuItemHolder menuItem = menuModule.getFileMenuItem().deserializeFromFile(
                                ctx, subElement, menuContext
                        );
                        items.put(ss, menuItem);
                    });
                    if(!items.isEmpty()) builder.put(s.toLowerCase(), Holder.directObject(items));
                    return;
                }
                case "view_requirement", "view_requirements", "view_condition", "view_conditions" ->{
                    ViewCondition viewCondition = ctx.getRegistry().deserializeFromFile(ViewCondition.class, element);
                    if(viewCondition != null){
                        builder.put(s, Holder.direct(viewCondition));
                        return;
                    }
                }
            }
            FileDataProvider provider = DATA_TYPES.get(s);
            if(provider != null){
                Object object = provider.deserialize(ctx, e, menuContext, element);
                if(object != null) builder.put(s, Holder.directObject(object));
                return;
            }
            if(!(element instanceof FileGeneric generic)) return;
            builder.put(s, Holder.directObject(generic.getAsObject()));
        });
        return builder.build();
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "data_exchange";
    }
}
