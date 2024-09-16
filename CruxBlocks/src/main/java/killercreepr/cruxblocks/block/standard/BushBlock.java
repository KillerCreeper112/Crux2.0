package killercreepr.cruxblocks.block.standard;

import killercreepr.cruxblocks.block.CruxBlock;
import org.jetbrains.annotations.NotNull;

public interface BushBlock extends CruxBlock {
    @NotNull BushType getBushType();
}
