package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.core.menu.module.PagedCruxCraftingRecipesMenuModule;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import killercreepr.cruxmenus.core.menu.module.standard.ActivePagedMenuModule;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericRecipeListMenu extends ConfigMenu {
    public GenericRecipeListMenu(@NotNull MenuHolder holder, @NotNull DataExchange info) {
        super(holder, info);
    }

    public GenericRecipeListMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags) {
        super(holder, info, tags);
    }

    @Override
    public void load() {
        super.load();
        var module = new PagedCruxCraftingRecipesMenuModule(
            "crafting_recipe_list",
            NumberProvider.uniformArray(
                NumberProvider.uniform(10, 16),
                NumberProvider.uniform(19, 25),
                NumberProvider.uniform(28, 34),
                NumberProvider.uniform(37, 43)
            ),
            null, null, null, Crux.key("crafting_recipe_list"), getRecipeManager()
        ).build(this);
        modules.register(module);
        module.load(this);

        addSlot(new SimpleFixedSlot(this, 48){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                ActivePagedMenuModule<?> paged = (ActivePagedMenuModule<?>) module;
                paged.addPage(-1);
            }
        });
        addSlot(new SimpleFixedSlot(this, 50){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                ActivePagedMenuModule<?> paged = (ActivePagedMenuModule<?>) module;
                paged.addPage(1);
            }
        });
    }

    public CruxCraftingRecipeManager getRecipeManager(){
        return info.getOrThrow("crafting_recipe_manager", CruxCraftingRecipeManager.class);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }
}
