package killercreepr.crux.api.block.tag;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface BlockTypeTag extends BlockTag {
    @NotNull
    Collection<Key> getTypes();
}
