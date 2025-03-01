package killercreepr.cruxenchants.api.crafting;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxenchants.core.crafting.SimpleCraftingMatrix;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface CruxCraftingMatrix extends Iterable<ItemStack> {
    static CruxCraftingMatrix craftingMatrix(ItemStack[] matrix){
        Map<Integer, ItemStack> map = new HashMap<>();
        Map<Integer, ItemStack> raw = new HashMap<>();

        int index = -1;
        int rawIndex = -1;
        for(ItemStack item : matrix){
            index++;
            map.put(index, item);
            if(CruxItem.isEmpty(item)) continue;
            rawIndex++;
            raw.put(rawIndex, item);
        }

        int height = matrix.length == 9 ? 3 : 2;
        int width = matrix.length == 9 ? 3 : 2;
        return new SimpleCraftingMatrix(map, raw, height, width);
    }

    boolean isEmpty();
    int getIngredientCount();
    int getHeight();
    int getWidth();

    @Nullable
    ItemStack getItem(int index);
    boolean hasItem(int index);

    @NotNull
    Map<Integer, ItemStack> toMatrixMap();
    @NotNull Map<Integer, ItemStack> getRawMatrix();
}
