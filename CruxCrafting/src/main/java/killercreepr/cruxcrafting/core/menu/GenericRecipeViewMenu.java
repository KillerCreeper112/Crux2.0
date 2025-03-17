package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxLoc;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxcrafting.api.crafting.CrafterHolder;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.crafter.CruxCraftingCrafter;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxCraftingRecipe;
import killercreepr.cruxmenus.CruxMenusModule;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.container.MenuContainer;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import net.kyori.adventure.key.Keyed;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.util.RayTraceResult;
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
        Entity viewer = info.get("viewer", Entity.class);
        if(viewer != null){
            CruxCraftingRecipeManager manager = getRecipeManager();
            cachedRecipes.removeIf(d -> !manager.hasRecipe(viewer, d));
        }
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
        var got = info.get("menu_container", MenuContainer.class);
        if(got == null){
            got = MenuContainer.createNew();
            got.addOpenedMenu(this);
            info(info.append("menu_container", Holder.direct(got)));
        }
        return got;
    }

    protected CraftingRecipeMenuViewer recipeViewer;

    @Override
    public void load() {
        super.load();
        menuContainer();
    }

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
        var p = event.getWhoClicked();
        if(recipeViewer.isResultSlot(slot)){
            MenuContainer container = menuContainer();
            if(container==null){
                clickCraftingTableNear(p);
                return;
            }
            CruxCraftingRecipe recipe = getRecipe();
            for (Menu menu : container.getOpenedMenus()) {
                if(!(menu instanceof CrafterHolder.Crafting crafter)) continue;
                if(menu instanceof CfgMenu cfgMenu){
                    cfgMenu.info(
                        info.append(cfgMenu.info()).append("selected_recipe", Holder.direct(recipe))
                    );
                    cfgMenu.refresh();
                }
                var viewer = crafter.buildRecipeViewer(recipe);
                container.addOpenedMenu(menu.open(p));
                viewer.display();
                crafter.getCrafter().updateCraftingInv();
                CreateSound.sound(Sound.BLOCK_DISPENSER_DISPENSE, 1.7f).playFor(p);
                return;
            }
            clickCraftingTableNear(p);
        }
    }

    public void clickCraftingTableNear(HumanEntity p){
        CruxCraftingRecipe recipe = getRecipe();
        if(recipe.ingredients().size() > 9) return;

        Block craftingTable = findNearestCraftingTable(p, p.getLocation().getBlock(), 6, 3);
        if(craftingTable==null){
            p.closeInventory();
            p.sendMessage(Crux.format().deserialize("<red>Unable to find nearby crafting table."));
            return;
        }

        InventoryView view = MenuType.CRAFTING.builder().location(craftingTable.getLocation()).checkReachable(true).build(p);
        p.openInventory(view);
        if(!view.equals(p.getOpenInventory())){
            p.sendMessage(Crux.format().deserialize("<red>Unable to open nearby crafting table."));
            return;
        }
        CraftingRecipeMenuViewer viewer = new CraftingTableRecipeViewer(view.getTopInventory(), recipe);
        viewer.display();
        CreateSound.sound(Sound.BLOCK_DISPENSER_DISPENSE, 1.5f).playFor(p);
        if(view.getTopInventory() instanceof CraftingInventory d){
            CruxCraftingCrafter crafter = CruxCraftingCrafter.craftingCrafter(getRecipeManager(), d);
            crafter.updateCraftingInv();
        }
    }

    public boolean isCraftingTable(Block b){
        return b.getType() == Material.CRAFTING_TABLE;
    }

    public boolean hasSightOf(Block b, Entity e){
        Location center = b.getLocation().toCenterLocation();
        double distance = center.distance(e.getLocation());
        Location lookingAt = CruxLoc.lookAt(center.clone(), e.getLocation().add(0, e.getHeight()/2, 0));
        RayTraceResult ray = b.getWorld().rayTrace(
            center, lookingAt.getDirection(), distance+1D, FluidCollisionMode.NEVER, true,
            0.1, check -> check.equals(e), check -> !check.equals(b)
        );
        if(ray == null) return false;
        return e.equals(ray.getHitEntity());
    }

    public boolean check(Entity e, Block b){
        if(!isCraftingTable(b)) return false;
        if(hasSightOf(b, e)) return true;
        return false;
    }

    public Block findNearestCraftingTable(Entity e, Block b, int range, int yRange){
        for(int x = -range; x <= range; x++){
            for(int z = -range; z <= range; z++){
                for(int y = -range; y <= yRange; y++){
                    Block check = b.getRelative(x, y, z);
                    if(check(e, check)) return check;
                }
            }
        }
        return null;
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
                if(menuContainer() ==null || menuContainer().getPrevious() == null){
                    p.closeInventory();
                    CreateSound.sound(Sound.UI_BUTTON_CLICK).playFor(p);
                    return;
                }
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
