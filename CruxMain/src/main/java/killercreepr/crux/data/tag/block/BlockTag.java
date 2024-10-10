package killercreepr.crux.data.tag.block;

import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.data.tag.Tag;
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
