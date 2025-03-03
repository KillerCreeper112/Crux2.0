package killercreepr.cruxcrafting.core.menu.module;

import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxmenus.CruxMenusModule;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.slot.Slot;
import killercreepr.cruxmenus.core.menu.module.standard.GenericActivePagedMenuModule;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ActivePagedCruxCraftingRecipeMenuModule extends GenericActivePagedMenuModule<CruxCraftingRecipe> {
    public ActivePagedCruxCraftingRecipeMenuModule(@NotNull String id, @NotNull MenuModule module,
                                                   @NotNull NumberProvider indexes,
                                                   @Nullable Predicate<CruxCraftingRecipe> valueFilter,
                                                   @NotNull Holder<List<CruxCraftingRecipe>> values) {
        super(id, module, indexes, valueFilter, values);
    }

    @Override
    public MergedTagContainer buildTags(CruxCraftingRecipe value) {
        return null;
    }

    @Override
    public void setPagedItem(@NotNull Menu menu, int slot, int index, int listIndex, @NotNull CruxCraftingRecipe value) {
        var display = value.getDisplayedResultItems();
        if(display.isEmpty()) return;
        menu.setItem(slot, display.getFirst(), buildPagedItemSlot(menu, slot, value));
    }

    public Slot buildPagedItemSlot(@NotNull Menu menu, int slot, @NotNull CruxCraftingRecipe value){
        return new SimpleFixedSlot(menu, slot){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                CruxMenusModule menus = CruxRegistries.MODULES.getModuleOrThrow(CruxMenusModule.class);
                var holder = menus.menuRegistry().menuHolders().get(Crux.key("crafting/recipe/view"));
                if(holder != null){
                    holder.open(p, DataExchange.builder()
                        .put("crafting_recipe", value)
                        .put("crafting_recipe_list_menu", ((CfgMenu) menu).getHolder())
                        .put("crafting_recipe_manager", ((PagedCruxCraftingRecipesMenuModule) module).getRecipeManager())
                        .build()
                    );
                    CreateSound.sound(Sound.UI_BUTTON_CLICK).playFor(p);
                }
            }
        };
    }

    @Override
    public void setEmptyItem(@NotNull Menu menu, int slot, int index) {

    }
}
