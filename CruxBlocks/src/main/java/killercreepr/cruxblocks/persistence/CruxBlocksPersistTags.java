package killercreepr.cruxblocks.persistence;

import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.persistence.PersistTag;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import net.kyori.adventure.key.Key;

public class CruxBlocksPersistTags {
    public static final PersistTag<CruxBlock> CRUX_BLOCK = new PersistTag<>(CruxBlocksPersistence.CRUX_BLOCK, "block");
    public static final PersistTag<Key> CRUX_BLOCK_KEY = new PersistTag<>(CruxPersistence.CRUX_KEY, "crux_block");
    public static final PersistTag<CruxBlockGroup> CRUX_BLOCK_GROUP = new PersistTag<>(CruxBlocksPersistence.CRUX_BLOCK_GROUP, "block_group");
}
