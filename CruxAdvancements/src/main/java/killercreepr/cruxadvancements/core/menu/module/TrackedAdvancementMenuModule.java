package killercreepr.cruxadvancements.core.menu.module;

import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.module.SimpleMenuModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TrackedAdvancementMenuModule extends SimpleMenuModule {
    protected final @NotNull String id;
    protected final @NotNull MenuModuleBuilder builder;
    protected final String advance;
    protected final int maxLevel;
    protected final MenuItems baseItems;
    protected final MenuItems trackedItems;
    protected final MenuItems maxItems;
    protected final int slot;
    public TrackedAdvancementMenuModule(@NotNull String id, @NotNull MenuModuleBuilder builder, String advance, int maxLevel,
                                        MenuItems baseItems, MenuItems trackedItems, MenuItems maxItems, int slot) {
        super(builder.key());
        this.id = id;
        this.builder = builder;
        this.advance = advance;
        this.maxLevel = maxLevel;
        this.baseItems = baseItems;
        this.trackedItems = trackedItems;
        this.maxItems = maxItems;
        this.slot = slot;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActiveTrackedAdvancementMenuModule(
            key, id, advance, maxLevel,
            baseItems, trackedItems, maxItems, slot
        );
    }
}
