package killercreepr.crux.data.tag.block;

import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.data.tag.Tag;
import killercreepr.crux.data.tag.item.SingleItemTypeTag;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public interface BlockTag extends Tag<CruxedBlock> {
    static BlockTag blockTag(@NotNull Key tagType, @NotNull Key type){
        return new SingleBlockTypeTag(tagType, type);
    }
}
