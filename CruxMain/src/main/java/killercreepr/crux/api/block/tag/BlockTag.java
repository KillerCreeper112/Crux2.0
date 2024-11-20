package killercreepr.crux.api.block.tag;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.block.tag.BukkitBlockTypeTag;
import killercreepr.crux.core.block.tag.SingleBlockTypeTag;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface BlockTag extends Tag<CruxedBlock> {
    static BlockTag blockTag(@NotNull Key tagType, @NotNull Key type){
        return new SingleBlockTypeTag(tagType, type);
    }
    static BlockTag blockTag(@NotNull org.bukkit.Tag<Material> tag){
        return new BukkitBlockTypeTag(tag);
    }
}
