package killercreepr.cruxblocks.persistence;

import killercreepr.crux.persistence.PersistTag;
import killercreepr.cruxblocks.block.CruxBlock;

public class CruxBlocksPersistTags {
    public static final PersistTag<CruxBlock> CRUX_BLOCK = new PersistTag<>(CruxBlocksPersistence.CRUX_BLOCK, "block");
}
