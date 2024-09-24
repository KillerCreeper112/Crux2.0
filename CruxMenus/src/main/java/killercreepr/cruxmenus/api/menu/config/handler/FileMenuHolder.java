package killercreepr.cruxmenus.api.menu.config.handler;

import killercreepr.cruxconfig.config.bukkit.handler.impl.item.FileDynamicItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.config.handlers.*;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileMenuHolder<T extends MenuHolder> extends FileObjectHandler<T> {
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @NotNull Key key);
    FileMenuItem getFileMenuItem();

    void setFileMenuItem(FileMenuItem fileMenuItem);

    FileMenuItems getFileMenuItems();

    void setFileMenuItems(FileMenuItems fileMenuItems);

    FileDataExchange getFileDataExchange();

    void setFileDataExchange(FileDataExchange fileDataExchange);

    FileMenuActions getFileMenuActions();

    void setFileMenuActions(FileMenuActions fileMenuActions);

    FileDynamicItem getFileDynamicItem();

    void setFileDynamicItem(FileDynamicItem fileItemStack);

    FileMenuMenuModule getFileMenuModule();

    void setFileMenuModule(FileMenuMenuModule fileMenuModule);
}
