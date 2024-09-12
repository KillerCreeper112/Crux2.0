package killercreepr.crux.block;

import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BukkitCruxedBlock implements CruxedBlock{
    protected final @NotNull Block block;
    public BukkitCruxedBlock(@NotNull Block block) {
        this.block = block;
    }

    @Override
    public @NotNull Key getType() {
        return block.getType().getKey();
    }
}
