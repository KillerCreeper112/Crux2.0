package killercreepr.cruxmenus.menu.bukkit.config.handlers;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.FileDynamicItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItems;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TreeMap;

public class FileMenuModule extends SimpleFileHandler<MenuHolder> {
    protected FileMenuItem fileMenuItem;
    protected FileMenuItems fileMenuItems;
    protected FileDataExchange fileDataExchange;
    protected FileMenuActions fileMenuActions;
    protected FileDynamicItem fileDynamicItem;
    protected FileMenuMenuModule fileMenuModule;
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull MenuHolder object) {
        throw new UnsupportedOperationException("MenuHolder has no serialized implementation!");
    }

    @Override
    public @Nullable MenuHolder deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();

        String id = o.getObject(String.class, "id");
        if(id == null) return null;
        String titleString = o.getObject(String.class, "title");
        String title = titleString == null ? "" : titleString;
        NumberProvider size = registry.deserialize(NumberProvider.class, o.get("size"));
        if(size == null) size = new ConstantNumber(27);
        DataExchange extraInfo = registry.deserialize(DataExchange.class, o.get("data"));

        MenuItems menuItems = getYamlMenuItems().deserializeFromFile(context, o.get("items"), o);
        if(menuItems == null) menuItems = new MenuItems(new TreeMap<>());

        Key key = Crux.key(id);
        return new MenuHolder(key, title, size,
            menuItems,
            extraInfo == null ? DataExchange.empty(): extraInfo
        );
    }

    public FileMenuItem getYamlMenuItem() {
        return fileMenuItem;
    }

    public void setYamlMenuItem(FileMenuItem fileMenuItem) {
        this.fileMenuItem = fileMenuItem;
    }

    public FileMenuItems getYamlMenuItems() {
        return fileMenuItems;
    }

    public void setYamlMenuItems(FileMenuItems fileMenuItems) {
        this.fileMenuItems = fileMenuItems;
    }

    public FileDataExchange getYamlDataExchange() {
        return fileDataExchange;
    }

    public void setYamlDataExchange(FileDataExchange fileDataExchange) {
        this.fileDataExchange = fileDataExchange;
    }

    public FileMenuActions getYamlMenuActions() {
        return fileMenuActions;
    }

    public void setYamlMenuActions(FileMenuActions fileMenuActions) {
        this.fileMenuActions = fileMenuActions;
    }

    public FileDynamicItem getYamlDynamicItem() {
        return fileDynamicItem;
    }

    public void setYamlDynamicItem(FileDynamicItem yamlItemStack) {
        this.fileDynamicItem = yamlItemStack;
    }

    public FileMenuMenuModule getFileMenuModule() {
        return fileMenuModule;
    }

    public void setFileMenuModule(FileMenuMenuModule fileMenuModule) {
        this.fileMenuModule = fileMenuModule;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_holder";
    }
}
