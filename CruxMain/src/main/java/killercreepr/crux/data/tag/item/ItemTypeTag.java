package killercreepr.crux.data.tag.item;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ItemTypeTag extends ItemTag {
    @NotNull
    Collection<Key> getTypes();
}
