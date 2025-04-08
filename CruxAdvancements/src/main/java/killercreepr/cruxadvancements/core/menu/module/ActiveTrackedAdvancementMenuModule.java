package killercreepr.cruxadvancements.core.menu.module;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.core.menu.module.SimpleActiveMenuModule;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActiveTrackedAdvancementMenuModule extends SimpleActiveMenuModule {
    protected final String advance;
    protected final int maxLevel;
    protected final MenuItems baseItems;
    protected final MenuItems trackedItems;
    protected final MenuItems maxItems;
    protected final int slot;

    public ActiveTrackedAdvancementMenuModule(@NotNull Key key, @NotNull String id, String advance, int maxLevel, MenuItems baseItems, MenuItems trackedItems, MenuItems maxItems, int slot) {
        super(key, id);
        this.advance = advance;
        this.maxLevel = maxLevel;
        this.baseItems = baseItems;
        this.trackedItems = trackedItems;
        this.maxItems = maxItems;
        this.slot = slot;
    }

    public ActiveTrackedAdvancementMenuModule(@NotNull Key key, String advance, int maxLevel, MenuItems baseItems, MenuItems trackedItems, MenuItems maxItems, int slot) {
        super(key);
        this.advance = advance;
        this.maxLevel = maxLevel;
        this.baseItems = baseItems;
        this.trackedItems = trackedItems;
        this.maxItems = maxItems;
        this.slot = slot;
    }

    @Override
    public void refresh(@NotNull Menu menu) {
        super.refresh(menu);
        if(!(menu instanceof CfgMenu m)) return;

        Player viewer = m.info().getOrThrow("viewer", Player.class);

        //String[] colonSplit = advance.split(":", 2);
        String[] valueArgs = advance.split("/", 2);
        Key managerKey = Crux.key(valueArgs[0]);

        AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(viewer, AdvancementHolder.class);
        for(int level = 1; level <= maxLevel; level++){
            Key advanceKey = Crux.key(advance + level);
            CruxAdvancement advance = AdvancementRegistries.ADVANCEMENT_MANAGERS.getAdvancement(managerKey, advanceKey);
            if(advance.isGranted(viewer)){
                continue;
            }
            if(holder != null && holder.getAdvancementTracker().isTracking(managerKey, advanceKey)){
                if(trackedItems != null){
                    //TrackedAdvancement advancement = holder.getAdvancementTracker().getNonGlobalTrackedAdvancement(managerKey, advanceKey);
                    m.setItems(trackedItems, MenuContext.context(m,
                        DataExchange.builder()
                            .put("advancement", advance)
                            .put("slot", slot)
                            .put("level", level)
                            .build(),
                        TagContainer.merged()));
                }
                return;
            }
            if(baseItems != null){
                m.setItems(baseItems, MenuContext.context(m,
                    DataExchange.builder()
                        .put("advancement", advance)
                        .put("slot", slot)
                        .put("level", level)
                        .build(),
                    TagContainer.merged()));
            }
            return;
        }
        if(maxItems != null){
            Key advanceKey = Crux.key(advance + maxLevel);
            CruxAdvancement advance = AdvancementRegistries.ADVANCEMENT_MANAGERS.getAdvancement(managerKey, advanceKey);
            m.setItems(maxItems, MenuContext.context(m,
                DataExchange.builder()
                    .put("advancement", advance)
                    .put("slot", slot)
                    .put("level", maxLevel)
                    .build(),
                TagContainer.merged()));
        }
    }
}
