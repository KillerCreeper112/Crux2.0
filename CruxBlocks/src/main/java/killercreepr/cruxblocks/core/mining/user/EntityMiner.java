package killercreepr.cruxblocks.core.mining.user;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityMiner extends ItemMiner {
    protected final @NotNull Entity entity;
    protected final @Nullable EquipmentSlot hand;
    public EntityMiner(@Nullable ItemStack tool, @NotNull Entity entity, @Nullable EquipmentSlot hand) {
        super(tool);
        this.entity = entity;
        this.hand = hand;
    }

    public EntityMiner(@NotNull Entity entity, @Nullable EquipmentSlot hand) {
        this(null, entity, hand);
    }

    public @Nullable EquipmentSlot getHand() {
        return hand;
    }

    public @NotNull Entity getEntity() {
        return entity;
    }

    @Override
    public Object getHandle() {
        return entity;
    }
}
