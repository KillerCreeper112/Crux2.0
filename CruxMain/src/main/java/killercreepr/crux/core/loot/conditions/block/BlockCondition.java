package killercreepr.crux.core.loot.conditions.block;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockCondition extends BaseCondition {
    protected final @Nullable BlockPredicate blockPredicate;
    protected final @Nullable LootCondition lightLevel;
    public BlockCondition(@NotNull String target, @Nullable BlockPredicate blockPredicate, @Nullable LootCondition lightLevel) {
        super(target);
        this.blockPredicate = blockPredicate;
        this.lightLevel = lightLevel;
    }

    public @Nullable BlockPredicate getBlockPredicate() {
        return blockPredicate;
    }

    public @Nullable LootCondition getLightLevel() {
        return lightLevel;
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
        if(b == null){
            Object o = ctx.info().get(target);
            if(o instanceof Entity e){
                b = e.getLocation().getBlock();
            }else if(o instanceof Location e){
                b = e.getBlock();
            }
        }
        if(b==null) return false;
        CruxedBlock cruxed = Crux.handlers().block().getBlock(b);
        if(blockPredicate != null && (cruxed == null || !blockPredicate.test(cruxed))) return false;
        if(lightLevel != null){
            if(!lightLevel.test(ctx.withInfo(ctx.info().appendObjects(Map.of(
                "block_light_level", b.getLightFromBlocks(),
                "light_level", b.getLightLevel(),
                "sky_light_level", b.getLightFromSky()
            ))))) return false;
        }
        return true;
    }
}
