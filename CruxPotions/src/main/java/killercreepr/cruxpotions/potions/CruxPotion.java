package killercreepr.cruxpotions.potions;

import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxPotion extends Keyed {
    /**
     * @return The formatted potion name.
     */
    @NotNull String getName();
    @NotNull PotionEffectType.Category getCategory();

    /**
     *
     * @param e The entity that the potion should be applied to.
     * @param duration The duration of the effect, in ticks.
     * @param amplifier The amplifier of the effect. 0 is "baseline".
     * @return The active potion effect that can then be properly applied to the entity.
     */
    default @NotNull ActivePotion create(@NotNull Entity e, int duration, int amplifier){
        return create(e, duration, amplifier, null);
    }

    /**
     *
     * @param e The entity that the potion should be applied to.
     * @param duration The duration of the effect, in ticks.
     * @param amplifier The amplifier of the effect. 0 is "baseline".
     * @param inflictor The object that inflicted the potion upon the entity.
     * @return The active potion effect that can then be properly applied to the entity.
     */
    @NotNull ActivePotion create(@NotNull Entity e, int duration, int amplifier, @Nullable PotionInflictor inflictor);
    default boolean compare(@Nullable CruxPotion with){
        if(with==null) return false;
        return compare(with.key());
    }
    default boolean compare(@Nullable Key with){
        return key().equals(with);
    }
}
