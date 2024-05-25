package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.Crux;
import killercreepr.crux.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class YamlMenuActions extends YamlModuled<Map<ClickType, Collection<String>>> {

    public YamlMenuActions(@NotNull YamlMenuModule menuModule) {
        super(menuModule);
    }

    public @Nullable Map<ClickType, Collection<String>> deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e,
                                                                            @Nullable MenuItemHolder base) {
        if(!(e instanceof YamlObject o)) return null;
        YamlRegistry registry = context.getRegistry();

        Map<ClickType, Collection<String>> map = base == null || base.getClickActions() == null ?
                new HashMap<>() : new HashMap<>(base.getClickActions());
        o.forEach((key, value) ->{
            ClickType type;
            try{ type = ClickType.valueOf(key.toUpperCase()); }
            catch (IllegalArgumentException ignored){
                Crux.log(Level.WARNING, "Click type of '" + key + "' not found!");
                return;
            }
            Collection<String> actions = registry.deserialize((Class<Collection<String>>) (Class<?>) Collection.class, o.get(key));
            if(actions == null || !actions.isEmpty()) map.put(type, actions);
        });
        return map.isEmpty() ? null : map;
    }

    @Override
    public @Nullable Map<ClickType, Collection<String>> deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable YamlObject menuContext) {
        throw new UnsupportedOperationException();//todo msg
    }
}
