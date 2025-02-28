package killercreepr.cruxblocks.core.block.component.standard;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.component.CruxBlockGroupComponent;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.block.active.standard.ActivePlaceableCheckBlock;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlaceableCheckComponent implements CruxBlockGroupComponent {
    protected final Map<BlockPos, BlockPredicate> filter;
    public PlaceableCheckComponent(Map<BlockPos, BlockPredicate> filter) {
        this.filter = filter;
    }

    public boolean isValid(Block block){
        for(var entry : filter.entrySet()){
            BlockPos offset = entry.getKey();
            BlockPredicate filter = entry.getValue();
            Block b = block.getRelative(offset.blockX(), offset.blockY(), offset.blockZ());
            if(!filter.test(Crux.handlers().block().getBlock(b))) return false;
        }
        return true;
    }

    @Override
    public @Nullable ActiveCruxBlock createActive(@NotNull Block block, @NotNull CruxBlock crux) {
        return new ActivePlaceableCheckBlock(block, crux, this);
    }

    @Override
    public @Nullable Boolean canPlace(@NotNull BlockContext ctx, @NotNull CruxBlockGroup group) {
        Block block = ctx.getBlock();
        return isValid(block);
    }
}
