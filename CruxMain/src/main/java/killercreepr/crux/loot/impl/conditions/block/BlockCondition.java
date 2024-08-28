package killercreepr.crux.loot.impl.conditions.block;

import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.impl.conditions.BaseCondition;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockCondition extends BaseCondition {
    protected final @Nullable Key blockType;
    public BlockCondition(@NotNull String target, @Nullable Key blockType) {
        super(target);
        this.blockType = blockType;
    }

    public @Nullable Key getBlockType() {
        return blockType;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Block b = ctx.info().get(target, Block.class);
        if(b==null) return false;
        if(blockType != null && !b.getType().key().equals(blockType)) return false;
        return true;
    }
}
