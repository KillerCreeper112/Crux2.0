package killercreepr.cruxenchants.core.crafting;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.cruxenchants.api.crafting.CruxCraftingMatrix;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

public class SimpleCraftingMatrix implements CruxCraftingMatrix {
    protected final Map<Integer, ItemStack> matrix;
    protected final Map<Integer, ItemStack> rawMatrix;
    protected final int height;
    protected final int width;

    public SimpleCraftingMatrix(Map<Integer, ItemStack> matrix, Map<Integer, ItemStack> rawMatrix, int height, int width) {
        this.matrix = matrix;
        this.rawMatrix = rawMatrix;
        this.height = height;
        this.width = width;
    }

    @Override
    public boolean isEmpty() {
        return matrix.isEmpty();
    }

    @Override
    public int getIngredientCount() {
        return matrix.size();
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public @Nullable ItemStack getItem(int index) {
        return matrix.get(index);
    }

    @Override
    public boolean hasItem(int index) {
        return !CruxItem.isEmpty(getItem(index));
    }

    @Override
    public @NotNull Map<Integer, ItemStack> toMatrixMap() {
        return matrix;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getRawMatrix() {
        return rawMatrix;
    }

    @Override
    public @NotNull Iterator<ItemStack> iterator() {
        return matrix.values().iterator();
    }
}
