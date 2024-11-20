package killercreepr.crux.core.block.tag;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.tag.BlockTag;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

public class BukkitBlockTypeTag implements BlockTag {
    protected final @NotNull Tag<Material> tag;
    public BukkitBlockTypeTag(@NotNull Tag<Material> tag) {
        this.tag = tag;
    }

    @Override
    public boolean isTagged(@NotNull CruxedBlock item) {
        Material m = Registry.MATERIAL.get(item.getType());
        if(m == null) return false;
        return tag.isTagged(m);
    }

    @Override
    public @NotNull Key key() {
        return tag.key();
    }
}
