package killercreepr.crux.core.loot.conditions.block;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BlockDirectionalNearbyCondition extends BaseCondition {
    protected final @NotNull LootCondition nearCondition;
    protected final @NotNull Collection<BlockFace> faces;
    public BlockDirectionalNearbyCondition(@NotNull String target, @NotNull LootCondition nearCondition, @NotNull Collection<BlockFace> faces) {
        super(target);
        this.nearCondition = nearCondition;
        this.faces = faces;
    }

    public @NotNull Collection<BlockFace> getFaces() {
        return faces;
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
        for(BlockFace face : faces){
            Block near = b.getRelative(face);
            if(nearCondition.test(ctx.withInfo(ctx.info().append("this", Holder.directObject(near))
                .append("direction", Holder.directObject(face))))) return true;
        }
        return false;
    }
}
