package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockAnyPredicate implements BlockPredicate, StringListEncodeComponent {
    protected final @NotNull Collection<BlockPredicate> children;
    public BlockAnyPredicate(@NotNull Collection<BlockPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull CruxedBlock block) {
        for(BlockPredicate predicate : children){
            if(predicate.test(block)) return true;
        }
        return false;
    }



    @Override
    public @NotNull List<String> encodeToParser() {
        List<String> list = new ArrayList<>();
        for(BlockPredicate predicate : children){
            if(!(predicate instanceof StringListEncodeComponent cc)) continue;
            list.addAll(cc.encodeToParser());
        }
        return list;
    }
}
