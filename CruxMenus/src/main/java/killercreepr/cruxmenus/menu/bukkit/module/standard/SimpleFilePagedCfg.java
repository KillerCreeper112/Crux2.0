package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.config.handlers.FileMenuHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItems;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleFilePagedCfg extends FilePagedCfg{
    protected final @NotNull Key key;
    public SimpleFilePagedCfg(@NotNull FileMenuHolder menuModule, @NotNull Key key) {
        super(menuModule);
        this.key = key;
    }

    @Override
    public @Nullable MenuModule parsePaged(@NotNull FileContext<?> ctx,
                                           @NotNull FileObject o,
                                           @Nullable FileObject menuContext,
                                           @NotNull String id,
                                           @NotNull NumberProvider indexes,
                                           @Nullable MenuItems valueItems,
                                           @Nullable MenuItems emptyItems) {
        return parsePaged(id, indexes, valueItems, emptyItems);
    }

    public abstract @Nullable MenuModule parsePaged(@NotNull String id,
                                           @NotNull NumberProvider indexes,
                                           @Nullable MenuItems valueItems,
                                           @Nullable MenuItems emptyItems);


    @Override
    public @NotNull Key key() {
        return key;
    }
}
