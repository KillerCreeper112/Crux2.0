package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxcrafting.api.crafting.CrafterHolder;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxmenus.CruxMenusModule;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.container.MenuContainer;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GenericRecipeViewMenu extends ConfigMenu {
    public GenericRecipeViewMenu(@NotNull MenuHolder holder, @NotNull DataExchange info) {
        super(holder, info);
    }

    public GenericRecipeViewMenu(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable MergedTagContainer tags) {
        super(holder, info, tags);
    }

    public List<CruxCraftingRecipe> cachedRecipes;

    public List<CruxCraftingRecipe> recipes(){
        if(cachedRecipes != null) return cachedRecipes;
        cachedRecipes = new ArrayList<>(getRecipeManager().getRecipes());
        cachedRecipes.sort(Comparator.comparing(recipe ->{
            if(recipe instanceof Keyed k) return k.key().value();
            return "a";
        }));
        return cachedRecipes;
    }

    public CruxCraftingRecipeManager getRecipeManager(){
        return info.getOrThrow("crafting_recipe_manager", CruxCraftingRecipeManager.class);
    }

    public CruxCraftingRecipe getRecipe(){
        return info.getOrThrow("crafting_recipe", CruxCraftingRecipe.class);
    }

    public @Nullable MenuContainer menuContainer(){
        return info.get("menu_container", MenuContainer.class);
    }

    protected CraftingRecipeMenuViewer recipeViewer;
    @Override
    public void onRefresh() {
        super.onRefresh();
        recipeViewer = new CraftingRecipeMenuViewer(inventory, getRecipe());
        recipeViewer.display();
        setButtons();
    }

    @Override
    public void onMenuClick(@NotNull InventoryClickEvent event) {
        super.onMenuClick(event);

        int slot = event.getSlot();
        if(recipeViewer.isResultSlot(slot)){
            MenuContainer container = menuContainer();
            if(container==null) return;
            for (Menu menu : container.getOpenedMenus()) {
                if(!(menu instanceof CrafterHolder.Crafting crafter)) continue;
                CruxCraftingRecipe recipe = getRecipe();
                var viewer = crafter.buildRecipeViewer(recipe);
                if(menu instanceof CfgMenu cfgMenu){
                    cfgMenu.info(cfgMenu.info().append("selected_recipe", Holder.direct(recipe)));
                }
                container.addOpenedMenu(menu.open(event.getWhoClicked()));
                viewer.display();
                crafter.getCrafter().updateCraftingInv();
                CreateSound.sound(Sound.BLOCK_DISPENSER_DISPENSE, 1.7f).playFor(event.getWhoClicked());
                return;
            }
        }
    }

    public void setButtons(){
        setItem(23, CruxItem.create(Material.ARROW)
            .itemName("Previous Recipe")
            .itemModel(Crux.key("gui/arrow_left"))
            .item(), new SimpleFixedSlot(this, 23){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                openPage(nextRecipe(-1), p);
            }
        });
        setItem(24, CruxItem.create(Material.ARROW)
            .itemName("Back")
            .itemModel(Crux.key("gui/arrow_down"))
            .item(), new SimpleFixedSlot(this, 24){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                menuContainer().back(p);
                CreateSound.sound(Sound.UI_BUTTON_CLICK).playFor(p);
            }
        });
        setItem(25, CruxItem.create(Material.ARROW)
            .itemName("Next Recipe")
            .itemModel(Crux.key("gui/arrow_right"))
            .item(), new SimpleFixedSlot(this, 25){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                openPage(nextRecipe(1), p);
            }
        });
    }
    public void openPage(CruxCraftingRecipe newRecipe, HumanEntity p){
        CruxMenusModule menus = CruxRegistries.MODULES.getModuleOrThrow(CruxMenusModule.class);
        menus.menuRegistry().menuHolders().get(Crux.key("crafting/recipe/view"))
            .open(p, info.append("crafting_recipe", Holder.direct(newRecipe)));
        CreateSound.sound(Sound.UI_BUTTON_CLICK).playFor(p);
    }

    public CruxCraftingRecipe nextRecipe(int add){
        List<CruxCraftingRecipe> list = recipes();
        int index = list.indexOf(getRecipe());
        index = CruxMath.wrap(index + add, 0, list.size()-1);
        return list.get(index);
    }
}
