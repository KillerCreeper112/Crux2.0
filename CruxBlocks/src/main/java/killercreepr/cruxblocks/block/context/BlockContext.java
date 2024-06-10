package killercreepr.cruxblocks.block.context;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockContext {
    @NotNull
    Block getBlock();
    @Nullable
    Entity getUser();
}
