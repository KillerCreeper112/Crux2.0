package killercreepr.crux.entity.predicate;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface EntityPredicate extends Predicate<Entity> {
    static EntityPredicate fromType(@NotNull Key type){
        return new EntityTypePredicate(type);
    }
    /*static EntityPredicate fromAllOf(@NotNull Collection<EntityPredicate> children){
        return new BlockAllPredicate(children);
    }
    static EntityPredicate fromAllOf(@NotNull EntityPredicate... children){
        return new BlockAllPredicate(Arrays.asList(children));
    }*/
    boolean test(@NotNull Entity entity);
}
