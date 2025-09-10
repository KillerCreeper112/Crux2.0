package killercreepr.crux.api.world.predicate;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.world.predicate.*;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public interface WorldPredicate extends Predicate<World> {
    static WorldPredicate fromKey(@NotNull Key key){
        return new WorldKeyPredicate(key);
    }
    static WorldPredicate fromAllOf(@NotNull Collection<WorldPredicate> children){
        return new WorldAllPredicate(children);
    }
    static WorldPredicate fromAllOf(@NotNull WorldPredicate... children){
        return new WorldAllPredicate(Arrays.asList(children));
    }
    static WorldPredicate fromLootCondition(@NotNull LootCondition loot){
        return new WorldLootConditionPredicate(loot);
    }
    static WorldPredicate fromAnyOf(@NotNull Collection<WorldPredicate> children){
        return new WorldAnyPredicate(children);
    }
    static WorldPredicate fromAnyOf(@NotNull WorldPredicate... children){
        return new WorldAnyPredicate(Arrays.asList(children));
    }
    static WorldPredicate fromTag(@NotNull Tag<World> tag){
        return new WorldTagPredicate(tag);
    }
    static WorldPredicate fromInverted(@NotNull WorldPredicate tag){
        return new WorldInvertPredicate(tag);
    }
    boolean test(@NotNull World world);
}
