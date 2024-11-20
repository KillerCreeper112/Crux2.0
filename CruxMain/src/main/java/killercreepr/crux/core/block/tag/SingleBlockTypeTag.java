package killercreepr.crux.core.block.tag;

import killercreepr.crux.api.block.CruxedBlock;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

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
