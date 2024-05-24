package killercreepr.crux.menu.bukkit.config.handlers;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.menu.bukkit.holder.MenuHolder;
import killercreepr.crux.menu.bukkit.holder.MenuItems;
import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlMenuModule implements YamlObjectHandler<MenuHolder> {
    protected YamlMenuItem yamlMenuItem;
    protected YamlMenuItems yamlMenuItems;
    protected YamlDataExchange yamlDataExchange;
    protected YamlMenuActions yamlMenuActions;

    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull MenuHolder object) {
        throw new UnsupportedOperationException("MenuHolder has no serialized implementation!");
    }

    @Override
    public @Nullable MenuHolder deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        YamlRegistry registry = context.getRegistry();

        String id = o.getObject(String.class, "id");
        String titleString = o.getObject(String.class, "title");
        String title = titleString == null ? "" : titleString;
        NumberProvider size = registry.deserialize(NumberProvider.class, o.get("size"));
        if(size == null) size = new ConstantNumber(27);
        DataExchange extraInfo = registry.deserialize(DataExchange.class, o.get("data"));

        MenuItems menuItems = yamlMenuItems.deserializeFromYaml(context, o.get("items"), o);

        NamespacedKey key = Crux.key(id);
        return new MenuHolder(key, title, size,
                menuItems,
                extraInfo == null ? DataExchange.empty(): extraInfo
        );
    }

    public YamlMenuItem getYamlMenuItem() {
        return yamlMenuItem;
    }

    public void setYamlMenuItem(YamlMenuItem yamlMenuItem) {
        this.yamlMenuItem = yamlMenuItem;
    }

    public YamlMenuItems getYamlMenuItems() {
        return yamlMenuItems;
    }

    public void setYamlMenuItems(YamlMenuItems yamlMenuItems) {
        this.yamlMenuItems = yamlMenuItems;
    }

    public YamlDataExchange getYamlDataExchange() {
        return yamlDataExchange;
    }

    public void setYamlDataExchange(YamlDataExchange yamlDataExchange) {
        this.yamlDataExchange = yamlDataExchange;
    }

    public YamlMenuActions getYamlMenuActions() {
        return yamlMenuActions;
    }

    public void setYamlMenuActions(YamlMenuActions yamlMenuActions) {
        this.yamlMenuActions = yamlMenuActions;
    }
}
