package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.GenericBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleCfgBlock extends GenericBlock implements CfgBlock {
    public SimpleCfgBlock(@NotNull Key key, @NotNull TextureData textureData) {
        super(key, textureData);
    }

    public @Nullable CfgBlockGroup getCfgGroup(){
        if(group instanceof CfgBlockGroup c) return c;
        return null;
    }

    @Override
    public @NotNull ActiveCruxBlock createActive(@NotNull Block block) {
        return new CfgActiveBlock(block, this);
    }
}
