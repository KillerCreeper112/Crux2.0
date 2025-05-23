package killercreepr.crux.core.block.tag;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.block.tag.BlockTypeTag;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class BukkitBlockTypeTag implements BlockTag, BlockTypeTag {
    protected final @NotNull Tag<Material> tag;
    protected final @NotNull Collection<Key> types;
    public BukkitBlockTypeTag(@NotNull Tag<Material> tag) {
        this.tag = tag;
        types = new HashSet<>();
        for(Material m : tag.getValues()){
            types.add(m.key());
        }
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

    @Override
    public @NotNull Collection<Key> getTypes() {
        return types;
    }
}
