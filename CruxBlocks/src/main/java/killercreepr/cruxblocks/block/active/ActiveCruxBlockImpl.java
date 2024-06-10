package killercreepr.cruxblocks.block.active;

import killercreepr.cruxblocks.block.CruxBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveCruxBlockImpl implements ActiveCruxBlock{
    protected final @NotNull Block block;
    protected final @NotNull CruxBlock cruxBlock;

    public ActiveCruxBlockImpl(@NotNull Block block, @NotNull CruxBlock cruxBlock) {
        this.block = block;
        this.cruxBlock = cruxBlock;
    }

    @Override
    public @NotNull Block getBlock() {
        return block;
    }

    @Override
    public @NotNull CruxBlock getCruxBlock() {
        return cruxBlock;
    }

    @Override
    public boolean isValid() {
        return cruxBlock.getTextureData().compareTexture(block);
    }

    @Override
    public void update() {

    }
}
