package killercreepr.crux.entity.predicate;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EntityAnyPredicate implements EntityPredicate {
    protected final @NotNull Collection<EntityPredicate> children;
    public EntityAnyPredicate(@NotNull Collection<EntityPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull Entity block) {
        for(EntityPredicate predicate : children){
            if(predicate.test(block)) return true;
        }
        return false;
    }
}
