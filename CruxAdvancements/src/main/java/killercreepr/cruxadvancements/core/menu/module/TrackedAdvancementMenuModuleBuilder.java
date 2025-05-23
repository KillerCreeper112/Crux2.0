package killercreepr.cruxadvancements.core.menu.module;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.config.handlers.SimpleFileMenuModuled;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TrackedAdvancementMenuModuleBuilder extends SimpleFileMenuModuled<MenuModule> implements MenuModuleBuilder {
    protected final @NotNull Key key;
    public TrackedAdvancementMenuModuleBuilder(@NotNull FileMenuHolder<?> menuModule, @NotNull Key key) {
        super(menuModule);
        this.key = key;
    }

    @Override
    public @Nullable MenuModule build(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return deserializeFromFile(ctx, e, menuContext);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @Nullable MenuModule deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable FileObject menuCtx) {
        if(!(e instanceof FileObject o)) return null;
        String id = o.getObject(String.class, "id");
        if(id==null) id = UUID.randomUUID().toString();
        String advance = o.getObject(String.class, "advance");
        int maxLevel = o.getOrDefaultObject(Integer.class, "max_levels", 1);

        MenuItems baseItems = menuModule.getFileMenuItems().deserializeFromFile(ctx, o.get("base_items"), menuCtx);
        MenuItems trackedItems = menuModule.getFileMenuItems().deserializeFromFile(ctx, o.get("tracked_items"), menuCtx);
        MenuItems maxItems = menuModule.getFileMenuItems().deserializeFromFile(ctx, o.get("max_items"), menuCtx);

        return new TrackedAdvancementMenuModule(id, this, advance, maxLevel,
            baseItems, trackedItems, maxItems, o.getOrDefaultObject(Integer.class, "slot", 0));
    }
}
