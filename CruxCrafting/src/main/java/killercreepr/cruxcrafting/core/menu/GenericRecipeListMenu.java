package killercreepr.cruxcrafting.core.menu;

import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import killercreepr.cruxcrafting.api.crafting.RecipeCategory;
import killercreepr.cruxcrafting.core.menu.module.PagedCruxCraftingRecipesMenuModule;
import killercreepr.cruxmenus.api.menu.container.MenuContainer;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.module.active.IActivePagedMenuModule;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import killercreepr.cruxmenus.core.menu.slot.SimpleFixedSlot;
import org.bukkit.Material;
import org.bukkit.Sound;
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

    public @Nullable MenuContainer menuContainer(){
        var got = info.get("menu_container", MenuContainer.class);
        if(got == null){
            got = MenuContainer.createNew();
            got.addOpenedMenu(this);
            info(info.append("menu_container", Holder.direct(got)));
        }
        return got;
    }

    public boolean showCategories(){
        return true;
    }

    protected IActivePagedMenuModule<?> pagedModule;
    @Override
    public void load() {
        var module = new PagedCruxCraftingRecipesMenuModule(
            "crafting_recipe_list",
            NumberProvider.uniformArray(
                NumberProvider.uniform(10, 16),
                NumberProvider.uniform(19, 25),
                NumberProvider.uniform(28, 34)
            ),
            null, null, null, Crux.key("crafting_recipe_list"), getRecipeManager()
        ).build(this);
        modules.register(module);
        pagedModule = (IActivePagedMenuModule<?>) module;
        menuContainer();
        super.load();
        //module.load(this);
    }

    public CruxCraftingRecipeManager getRecipeManager(){
        return info.getOrThrow("crafting_recipe_manager", CruxCraftingRecipeManager.class);
    }

    /**
     * Sets the MenuHolder's items and click actions.
     *
     * @param holder
     */
    @Override
    public void setItems(@NotNull MenuHolder holder) {
        super.setItems(holder);
        if(pagedModule != null){
            if(pagedModule.getPage() > 0){
                setItem(inventory.getSize()-6, CruxItem.create(Material.ARROW)
                    .itemName("Previous Page")
                    .itemModel(Crux.key("gui/arrow_left"))
                    .item());
            }else setItem(inventory.getSize()-6, CruxItem.create(Material.AIR).item());
            if(pagedModule.getPage() < pagedModule.getMaxPage()){
                setItem(inventory.getSize()-4, CruxItem.create(Material.ARROW)
                    .itemName("Next Page")
                    .itemModel(Crux.key("gui/arrow_right"))
                    .item());
            }else setItem(inventory.getSize()-4, CruxItem.create(Material.AIR).item());
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        addSlot(new SimpleFixedSlot(this, inventory.getSize()-6){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                IActivePagedMenuModule<?> paged = pagedModule;
                int oldPage = paged.getPage();
                paged.addPage(-1);
                if(oldPage == paged.getPage()) return;
                refresh();
                CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
            }
        });
        addSlot(new SimpleFixedSlot(this, inventory.getSize()-4){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                IActivePagedMenuModule<?> paged = pagedModule;
                int oldPage = paged.getPage();
                paged.addPage(1);
                if(oldPage == paged.getPage()) return;
                refresh();
                CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
            }
        });
        if(showCategories()) setupCategories();

        setItem(inventory.getSize()-5, CruxItem.create(Material.ARROW)
            .itemName("Back")
            .itemModel(Crux.key("gui/arrow_down"))
            .item(), new SimpleFixedSlot(this, inventory.getSize()-5){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                var previous = menuContainer();
                if(previous==null || previous.getPrevious() == null){
                    p.closeInventory();
                    CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
                    return;
                }
                previous.back(p);
                CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
            }
        });
    }

    public void setupCategories(){
        setItem(1, CruxItem.create(Material.COMPASS)
            .itemName("<white>All")
            .addLoreFromString(
                "<gray>Show all recipes.",
                "",
                "<yellow><latinfont:Click to view all>"
            )
            .item(), new SimpleFixedSlot(this, 1){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                if(!info.has("selected_recipe_category")) return;
                info(info.remove("selected_recipe_category"));
                pagedModule.setPage(0);
                refresh();
                CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
            }
        });
        setItem(3, CruxItem.create(Material.IRON_SWORD)
            .itemName("<white>Equipment")
            .addLoreFromString(
                "<gray>Show only recipes that",
                "<gray>are in the <white>Equipment",
                "<gray>category.",
                "",
                "<yellow><latinfont:Click to filter>"
            )
            .hideAttributes()
            .item(), new SimpleFixedSlot(this, 3){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                info(info.append("selected_recipe_category", Holder.direct(RecipeCategory.EQUIPMENT)));
                pagedModule.setPage(0);
                refresh();
                CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
            }
        });
        setItem(4, CruxItem.create(Material.BRICKS)
            .itemName("<white>Building")
            .addLoreFromString(
                "<gray>Show only recipes that",
                "<gray>are in the <white>Building",
                "<gray>category.",
                "",
                "<yellow><latinfont:Click to filter>"
            )
            .item(), new SimpleFixedSlot(this, 4){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                info(info.append("selected_recipe_category", Holder.direct(RecipeCategory.BUILDING)));
                pagedModule.setPage(0);
                refresh();
                CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
            }
        });
        setItem(5, CruxItem.create(Material.LAVA_BUCKET)
            .itemName("<white>Misc")
            .addLoreFromString(
                "<gray>Show only recipes that",
                "<gray>are in the <white>Misc",
                "<gray>category.",
                "",
                "<yellow><latinfont:Click to filter>"
            )
            .item(), new SimpleFixedSlot(this, 5){
            @Override
            public void onClick(@NotNull HumanEntity p, @NotNull InventoryClickEvent event) {
                super.onClick(p, event);
                info(info.append("selected_recipe_category", Holder.direct(RecipeCategory.MISC)));
                pagedModule.setPage(0);
                refresh();
                CreateSound.sound(Sound.UI_BUTTON_CLICK, 0.2f, 1f).playFor(p);
            }
        });
    }
}
