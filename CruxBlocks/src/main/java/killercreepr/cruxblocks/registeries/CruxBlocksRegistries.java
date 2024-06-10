package killercreepr.cruxblocks.registeries;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;

public class CruxBlocksRegistries {
    public static final MappedRegistry<TextureData, CruxBlock> TEXTURE_DATA_TO_CRUX_BLOCK = new SimpleMappedRegistry<>();
    public static final KeyedRegistry<CruxBlock> BLOCKS = new SimpleKeyedRegistry<>();
    public static final KeyedRegistry<CruxBlockGroup> BLOCK_GROUPS = new SimpleKeyedRegistry<>();
}
