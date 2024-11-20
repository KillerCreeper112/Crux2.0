package killercreepr.crux.api.entity.tag;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.entity.tag.SimpleEntityTypeTag;
import killercreepr.crux.core.entity.tag.SingleEntityTypeTag;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface EntityTag extends Tag<Entity> {
    static EntityTag entityTag(@NotNull Key tagType, @NotNull Key type){
        return new SingleEntityTypeTag(tagType, type);
    }
    static EntityTag entityTag(@NotNull Key key, @NotNull Collection<Key> values){
        return new SimpleEntityTypeTag(key, values);
    }
}
