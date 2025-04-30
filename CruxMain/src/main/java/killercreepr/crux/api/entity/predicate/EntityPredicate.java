package killercreepr.crux.api.entity.predicate;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.entity.predicate.*;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public interface EntityPredicate extends Predicate<Entity> {
    static EntityPredicate fromType(@NotNull Key type){
        return new EntityTypePredicate(type);
    }
    static EntityPredicate fromAllOf(@NotNull Collection<EntityPredicate> children){
        return new EntityAllPredicate(children);
    }
    static EntityPredicate fromAllOf(@NotNull EntityPredicate... children){
        return new EntityAllPredicate(Arrays.asList(children));
    }
    static EntityPredicate fromAnyOf(@NotNull Collection<EntityPredicate> children){
        return new EntityAnyPredicate(children);
    }
    static EntityPredicate fromAnyOf(@NotNull EntityPredicate... children){
        return new EntityAnyPredicate(Arrays.asList(children));
    }
    static EntityPredicate fromTag(@NotNull Tag<Entity> tag){
        return new EntityTagPredicate(tag);
    }
    static EntityPredicate fromInverted(@NotNull EntityPredicate tag){
        return new EntityInvertPredicate(tag);
    }
    boolean test(@NotNull Entity entity);
}
