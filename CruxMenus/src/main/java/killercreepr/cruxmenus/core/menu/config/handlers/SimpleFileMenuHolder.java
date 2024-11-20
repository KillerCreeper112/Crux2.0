package killercreepr.cruxmenus.core.menu.config.handlers;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.core.valueproviders.number.ConstantNumber;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.FileDynamicItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class SimpleFileMenuHolder extends SimpleFileHandler<MenuHolder> implements FileMenuHolder<MenuHolder> {
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
        String id = o.getObject(String.class, "id");
        if(id == null) return null;
        return deserializeFromFile(context, e, Crux.key(id));
    }

    public @Nullable MenuHolder deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();

        String titleString = o.getObject(String.class, "title");
        String title = titleString == null ? "" : titleString;
        NumberProvider size = registry.deserializeFromFile(NumberProvider.class, o.get("size"));
        if(size == null) size = new ConstantNumber(27);
        DataExchange extraInfo = registry.deserializeFromFile(DataExchange.class, o.get("data"));

        MenuItems menuItems = getFileMenuItems().deserializeFromFile(context, o.get("items"), o);
        if(menuItems == null) menuItems = MenuItems.items(new TreeMap<>());

        Collection<MenuModule> modules = new ArrayList<>();
        if(o.get("modules") instanceof FileObject mods){
            mods.forEach((string, ele) ->{
                MenuModule module = fileMenuModule.deserializeFromFile(context, ele, o);
                if(module != null) modules.add(module);
            });
        }else if(o.get("modules") instanceof FileArray mods){
            mods.forEach(ele ->{
                MenuModule module = fileMenuModule.deserializeFromFile(context, ele, o);
                if(module != null) modules.add(module);
            });
        }

        return MenuHolder.holder(key, title, size,
            menuItems,
            extraInfo == null ? DataExchange.empty(): extraInfo,
            modules
        );
    }

    @Override
    public FileMenuItem getFileMenuItem() {
        return fileMenuItem;
    }

    @Override
    public void setFileMenuItem(FileMenuItem fileMenuItem) {
        this.fileMenuItem = fileMenuItem;
    }

    @Override
    public FileMenuItems getFileMenuItems() {
        return fileMenuItems;
    }

    @Override
    public void setFileMenuItems(FileMenuItems fileMenuItems) {
        this.fileMenuItems = fileMenuItems;
    }

    @Override
    public FileDataExchange getFileDataExchange() {
        return fileDataExchange;
    }

    @Override
    public void setFileDataExchange(FileDataExchange fileDataExchange) {
        this.fileDataExchange = fileDataExchange;
    }

    @Override
    public FileMenuActions getFileMenuActions() {
        return fileMenuActions;
    }

    @Override
    public void setFileMenuActions(FileMenuActions fileMenuActions) {
        this.fileMenuActions = fileMenuActions;
    }

    @Override
    public FileDynamicItem getFileDynamicItem() {
        return fileDynamicItem;
    }

    @Override
    public void setFileDynamicItem(FileDynamicItem fileDynamicItem) {
        this.fileDynamicItem = fileDynamicItem;
    }

    @Override
    public FileMenuMenuModule getFileMenuModule() {
        return fileMenuModule;
    }

    @Override
    public void setFileMenuModule(FileMenuMenuModule fileMenuModule) {
        this.fileMenuModule = fileMenuModule;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_holder";
    }
}
