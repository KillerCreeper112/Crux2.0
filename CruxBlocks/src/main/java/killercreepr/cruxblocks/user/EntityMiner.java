package killercreepr.cruxblocks.user;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityMiner extends ItemMiner {
    protected final @NotNull Entity entity;
    public EntityMiner(@Nullable ItemStack tool, @NotNull Entity entity) {
        super(tool);
        this.entity = entity;
    }

    public EntityMiner(@NotNull Entity entity) {
        this(null, entity);
    }

    public @NotNull Entity getEntity() {
        return entity;
    }

    @Override
    public Object getHandle() {
        return entity;
    }
}
