package killercreepr.crux.loot.conditions.entity;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.conditions.BaseCondition;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityCondition extends BaseCondition {
    protected final @Nullable Key entityType;
    protected final @Nullable String worldName;
    public EntityCondition(@NotNull String target, @Nullable Key entityType, @Nullable String worldName) {
        super(target);
        this.entityType = entityType;
        this.worldName = worldName;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Entity e = ctx.info().get(target, Entity.class);
        if(e==null) return false;
        if(entityType != null && !e.getType().key().equals(entityType)) return false;
        if(worldName != null && !e.getWorld().getName().equals(worldName)) return false;
        return true;
    }
}
