package killercreepr.cruxblocks.persistence;

import killercreepr.crux.persistence.PersistTag;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;

public class CruxBlocksPersistTags {
    public static final PersistTag<CruxBlock> CRUX_BLOCK = new PersistTag<>(CruxBlocksPersistence.CRUX_BLOCK, "block");
    public static final PersistTag<CruxBlockGroup> CRUX_BLOCK_GROUP = new PersistTag<>(CruxBlocksPersistence.CRUX_BLOCK_GROUP, "block_group");
}
