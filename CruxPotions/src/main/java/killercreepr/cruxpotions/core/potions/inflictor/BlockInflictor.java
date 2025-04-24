package killercreepr.cruxpotions.core.potions.inflictor;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockInflictor implements PotionInflictor, Holder<Block> {
    public static final String ID = "block";
    protected final @NotNull Key world;
    protected final @NotNull BlockPos pos;

    public BlockInflictor(@NotNull Key world, @NotNull BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public BlockInflictor(Block b) {
        this.world = b.getWorld().key();
        this.pos = BlockPos.at(b);
    }

    public @NotNull Key getWorld() {
        return world;
    }

    public @NotNull BlockPos getPos() {
        return pos;
    }

    @Override
    public Block value() {
        World world = Crux.getServer().getWorld(this.world);
        return world == null ? null : pos.getBlock(world);
    }

    /**
     * Used for file serialization
     */
    @Override
    public @NotNull String getTypeID() {
        return ID;
    }
}
