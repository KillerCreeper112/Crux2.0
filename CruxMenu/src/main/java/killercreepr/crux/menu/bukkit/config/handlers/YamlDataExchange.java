package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class YamlDataExchange extends YamlModuled<DataExchange> {
    public YamlDataExchange(@NotNull YamlMenuModule menuModule) {
        super(menuModule);
    }

    @Override
    public @Nullable DataExchange deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable YamlObject menuContext) {
        if(!(e instanceof YamlObject o)) return null;
        DataExchange.Builder builder = new DataExchange.Builder();
        o.forEach((s, element) ->{
            switch (s.toLowerCase()){
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
            if(!(element instanceof YamlGeneric generic)) return;
            builder.put(s.toLowerCase(), Holder.directObject(generic.getAsObject()));
        });
        return builder.build();
    }
}
