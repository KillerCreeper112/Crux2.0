package killercreepr.crux.core.loot.conditions.entity;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.util.CruxTag;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EntityPersistTagsCondition extends BaseCondition {
    protected final @Nullable Collection<String> tags;
    public EntityPersistTagsCondition(@NotNull String target, @Nullable Collection<String> tags) {
        super(target);
        this.tags = tags;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Entity e = ctx.info().get(target, Entity.class);
        if(e==null) return false;

        if(tags != null){
            for (String tag : tags) {
                if(!CruxTag.has(e, tag)) return false;
            }
        }

        return true;
    }
}
