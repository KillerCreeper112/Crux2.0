package killercreepr.cruxpotions.core.potions.inflictor;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class BlockInflictor implements PotionInflictor, Holder<Block> {
    protected final @NotNull Block block;
    public BlockInflictor(@NotNull World world, @NotNull Vector vector) {
        this.block = world.getBlockAt(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public BlockInflictor(@NotNull Block block) {
        this.block = block;
    }

    @Override
    public @NotNull Block value() {
        return block;
    }
}
