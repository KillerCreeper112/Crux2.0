package killercreepr.crux.core.block.tag;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.tag.BlockTypeTag;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SingleBlockTypeTag extends BaseBlockTag implements BlockTypeTag {
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

    @Override
    public @NotNull Collection<Key> getTypes() {
        return Set.of(value);
    }
}
