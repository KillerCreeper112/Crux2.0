package killercreepr.cruxblocks.block.context;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericBlockContext implements BlockContext{
    protected final @NotNull Block block;
    protected final @Nullable Entity user;
    public GenericBlockContext(@NotNull Block block, @Nullable Entity user) {
        this.block = block;
        this.user = user;
    }

    @Override
    public @NotNull Block getBlock() {
        return block;
    }

    @Override
    public @Nullable Entity getUser() {
        return user;
    }
}
