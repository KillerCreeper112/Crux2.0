package killercreepr.crux.core.loot.conditions.block;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;

public class BlockDirectionalInfoCondition extends BaseCondition {
    protected final BlockFace defaultDirection;
    protected final boolean opposite;

    public BlockDirectionalInfoCondition(@NotNull String target, BlockFace defaultDirection, boolean opposite) {
        super(target);
        this.defaultDirection = defaultDirection;
        this.opposite = opposite;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Block b = ctx.info().get(target, Block.class);
        if(b == null){
            var state = ctx.info().get(target, BlockState.class);
            if(state == null) return false;
            try{
                b = state.getBlock();
            }catch (IllegalStateException ignored){}
        }
        if(b==null) return false;
        if(!(b.getBlockData() instanceof Directional dir)) return false;
        BlockFace direction = ctx.info().getOrDefault("direction", BlockFace.class, defaultDirection);
        if(direction == null) return false;
        return dir.getFacing() == direction;
    }
}
