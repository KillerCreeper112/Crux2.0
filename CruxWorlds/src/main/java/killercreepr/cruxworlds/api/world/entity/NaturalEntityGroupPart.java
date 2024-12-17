package killercreepr.cruxworlds.api.world.entity;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface NaturalEntityGroupPart {
    boolean isPartOfGroup(@NotNull Entity e);
}
