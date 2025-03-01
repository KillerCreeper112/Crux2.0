package killercreepr.cruxcrafting.core.crafting.crafter;

import killercreepr.cruxcrafting.api.crafting.CruxCraftingRecipeManager;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CraftingInvCrafter extends SimpleCraftingCrafter{
    protected final CraftingInventory craftInv;
    protected final Plugin plugin;
    protected final int delay;
    public CraftingInvCrafter(CruxCraftingRecipeManager craftingManager, CraftingInventory inv) {
        this(craftingManager, inv, null, 0);
    }

    public CraftingInvCrafter(CruxCraftingRecipeManager craftingManager, CraftingInventory inv, Plugin plugin, int delay) {
        super(craftingManager, inv);
        this.craftInv = inv;
        this.plugin = plugin;
        this.delay = delay;
    }

    @Override
    public void onHandleCraftingEnd(){
        if(plugin != null && delay > 0){
            plugin.getServer().getScheduler().runTaskLater(plugin, this::updateCraftingInv, delay);
            return;
        }
        updateCraftingInv();
    }

    @Override
    public ItemStack[] getMatrix() {
        return craftInv.getMatrix();
    }

    @Override
    public void setResults(List<ItemStack> items) {
        if(items == null || items.isEmpty()){
            craftInv.setResult(null);
            return;
        }
        craftInv.setResult(items.getFirst());
    }

    @Override
    public List<ItemStack> getResults() {
        ItemStack result = craftInv.getResult();
        if(result==null) return List.of();
        return List.of(result);
    }
}
