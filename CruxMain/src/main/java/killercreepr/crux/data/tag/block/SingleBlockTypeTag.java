package killercreepr.crux.data.tag.block;

import killercreepr.crux.Crux;
import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.item.ItemHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SingleBlockTypeTag extends BaseBlockTag {
    protected final @NotNull Key value;
    public SingleBlockTypeTag(@NotNull Key key, @NotNull Key value) {
        super(key);
        this.value = value;
    }

    @Override
    public boolean isTagged(@NotNull CruxedBlock item) {
        return value.equals(item.getType());
    }

    public @NotNull Key getType() {
        return value;
    }
}
