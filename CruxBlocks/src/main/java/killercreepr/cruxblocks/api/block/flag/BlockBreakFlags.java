package killercreepr.cruxblocks.api.block.flag;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public class BlockBreakFlags {
    public static BlockBreakFlags empty(){
        return new BlockBreakFlags();
    }

    public static BlockBreakFlags standard(){
        return flags(BlockBreakFlag.DISABLE_PARTICLES);
    }

    public static BlockBreakFlags flags(BlockBreakFlag... flags){
        return empty().add(flags);
    }

    protected final Collection<BlockBreakFlag> flags = new HashSet<>();
    public boolean hasFlag(BlockBreakFlag flag){
        return flags.contains(flag);
    }
    public BlockBreakFlags add(BlockBreakFlag flag){
        flags.add(flag);
        return this;
    }
    public BlockBreakFlags add(BlockBreakFlag... flag){
        for(BlockBreakFlag f : flag){
            add(f);
        }
        return this;
    }
    public BlockBreakFlags apply(Consumer<BlockBreakFlags> consumer){
        consumer.accept(this);
        return this;
    }
}
