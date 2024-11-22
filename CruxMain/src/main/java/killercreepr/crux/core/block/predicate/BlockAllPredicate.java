package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.predicate.BlockPredicateComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockAllPredicate implements BlockPredicateComponent {
    protected final @NotNull Collection<BlockPredicate> children;
    public BlockAllPredicate(@NotNull Collection<BlockPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull CruxedBlock block) {
        for(BlockPredicate predicate : children){
            if(!predicate.test(block)) return false;
        }
        return true;
    }

    public @NotNull Collection<BlockPredicate> getChildren() {
        return children;
    }

    @Override
    public @NotNull List<String> encodeToParser() {
        List<String> list = new ArrayList<>();
        for(BlockPredicate predicate : children){
            if(!(predicate instanceof BlockPredicateComponent cc)) continue;
            list.addAll(cc.encodeToParser());
        }
        return list;
    }
}
