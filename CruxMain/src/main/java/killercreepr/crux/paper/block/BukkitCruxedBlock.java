package killercreepr.crux.paper.block;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.math.CruxPosition;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BukkitCruxedBlock implements CruxedBlock {
    protected final @NotNull Block block;
    public BukkitCruxedBlock(@NotNull Block block) {
        this.block = block;
    }

    @Override
    public @NotNull Key getType() {
        return block.getType().getKey();
    }

    @Override
    public @NotNull CruxPosition getPosition() {
        return CruxPosition.block(block);
    }

    @Override
    public int getX() {
        return block.getX();
    }

    @Override
    public int getY() {
        return block.getY();
    }

    @Override
    public int getZ() {
        return block.getZ();
    }

    @Override
    public @NotNull Block getBlock() {
        return block;
    }
}
