package killercreepr.crux.loot.conditions.block;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.conditions.BaseCondition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockCondition extends BaseCondition {
    protected final @Nullable Material type;
    public BlockCondition(@NotNull String target, @Nullable Material type) {
        super(target);
        this.type = type;
    }

    public @Nullable Material getType() {
        return type;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Block b = ctx.info().get(target, Block.class);
        if(b==null) return false;
        if(type != null && b.getType() != type) return false;
        return true;
    }
}
