package killercreepr.cruxmenu.menu.bukkit.config.handlers;

import killercreepr.cruxmenu.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuItems;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class YamlMenuItems extends YamlModuled<MenuItems> {
    public YamlMenuItems(@NotNull YamlMenuModule menuModule) {
        super(menuModule);
    }

    public @Nullable MenuItems deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e, @Nullable YamlObject menuContext) {
        if(!(e instanceof YamlObject o)) return null;
        MenuItems map = new MenuItems(new TreeMap<>());
        o.forEach((key, value) ->{
            MenuItemHolder item = menuModule.getYamlMenuItem().deserializeFromYaml(context, value, menuContext);
            if(item != null) map.add(item);
        });
        return map;
    }
}
