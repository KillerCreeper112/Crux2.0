package killercreepr.cruxpotions.api.potion;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxPotion extends CruxKeyed {
    /**
     * @return The formatted potion name.
     */
    @NotNull String getName();
    @NotNull PotionEffectType.Category getCategory();

    default @Nullable String getDescription(){
        return null;
    }

    /**
     * A null value signifies that this potion will not have any effect
     * on a potion's color.
     */
    default @Nullable Color getColor(){
        return null;
    }

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

    default StoredPotion deserializeFromFile(int duration, int amplifier, @Nullable FileObject data){
        return StoredPotion.storedPotion(this, duration, amplifier);
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
}
