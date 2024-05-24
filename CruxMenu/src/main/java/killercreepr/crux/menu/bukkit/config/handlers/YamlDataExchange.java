package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class YamlDataExchange implements YamlObjectHandler<DataExchange> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull DataExchange object) {
        throw new UnsupportedOperationException("DataExchange may not be serialized!");
    }

    @Override
    public @Nullable DataExchange deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        YamlRegistry registry = context.getRegistry();
        DataExchange.Builder builder = new DataExchange.Builder();
        o.forEach((s, element) ->{
            switch (s.toLowerCase()){
                case "items" ->{
                    Map<String, MenuItemHolder> items = new HashMap<>();
                    if(!(element instanceof YamlObject subSection)) return;
                    subSection.forEach((ss, subElement) ->{
                        //todo
                        if(!(registry.deserializeObject(MenuItemHolder.class, subElement) instanceof MenuItemHolder menuItem)) return;
                        items.put(ss, menuItem);
                    });
                    if(!items.isEmpty()) builder.put(s.toLowerCase(), Holder.directObject(items));
                    return;
                }
            }
            if(!(element instanceof YamlGeneric generic)) return;
            builder.put(s.toLowerCase(), Holder.directObject(generic.getAsObject()));
        });
        return builder.build();
    }
}
