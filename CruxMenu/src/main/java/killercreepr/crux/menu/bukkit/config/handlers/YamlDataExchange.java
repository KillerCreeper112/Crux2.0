package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.config.YamlDataProvider;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class YamlDataExchange extends YamlModuled<DataExchange> {
    protected final MappedRegistry<String, YamlDataProvider> DATA_TYPES = new SimpleMappedRegistry<>();
    public YamlDataExchange(@NotNull YamlMenuModule menuModule) {
        super(menuModule);
    }

    public MappedRegistry<String, YamlDataProvider> getDataTypes() {
        return DATA_TYPES;
    }

    @Override
    public @Nullable DataExchange deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable YamlObject menuContext) {
        if(!(e instanceof YamlObject o)) return null;
        DataExchange.Builder builder = new DataExchange.Builder();
        o.forEach((s, element) ->{
            s = s.toLowerCase();
            switch (s){
                case "items" ->{
                    Map<String, MenuItemHolder> items = new HashMap<>();
                    if(!(element instanceof YamlObject subSection)) return;
                    subSection.forEach((ss, subElement) ->{
                        MenuItemHolder menuItem = menuModule.getYamlMenuItem().deserializeFromYaml(
                                context, subElement, menuContext
                        );
                        items.put(ss, menuItem);
                    });
                    if(!items.isEmpty()) builder.put(s.toLowerCase(), Holder.directObject(items));
                    return;
                }
            }
            YamlDataProvider provider = DATA_TYPES.get(s);
            if(provider != null){
                Object object = provider.deserialize(context, e, menuContext, element);
                if(object != null) builder.put(s, Holder.directObject(object));
                return;
            }
            if(!(element instanceof YamlGeneric generic)) return;
            builder.put(s, Holder.directObject(generic.getAsObject()));
        });
        return builder.build();
    }
}
