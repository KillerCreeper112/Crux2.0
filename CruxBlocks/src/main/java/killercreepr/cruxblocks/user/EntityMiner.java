package killercreepr.cruxblocks.user;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityMiner extends ItemMiner {
    public static @NotNull EntityMiner from(@NotNull Entity entity){
        ItemStack tool = null;
        if(entity instanceof LivingEntity e){
            EntityEquipment equipment = e.getEquipment();
            if(equipment != null) tool = equipment.getItemInMainHand();
        }
        return new EntityMiner(tool, entity);
    }

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

}
