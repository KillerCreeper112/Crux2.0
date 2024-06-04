package killercreepr.cruxpotions.potions;

import killercreepr.crux.util.CruxString;
import killercreepr.cruxpotions.CruxPotionsModule;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class GenericPotion implements CruxPotion {
    protected final @NotNull Key key;
    public GenericPotion(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull String getName(){
        return CruxString.toTitleCase(key.value());
    }

    protected @NotNull PotionEffectType.Category defaultCategory(){ return PotionEffectType.Category.NEUTRAL; }

    @Override
    public @NotNull PotionEffectType.Category getCategory(){
        Map<Key, PotionEffectType.Category> value = CruxPotionsModule.cfg().POTION_CATEGORIES.value();
        if(value == null) return defaultCategory();
        return value.getOrDefault(key, defaultCategory());
    }
    @Override
    public abstract @NotNull ActivePotion create(@NotNull Entity e, int duration, int amplifier, @Nullable PotionInflictor inflictor);

    @Override
    public @NotNull Key key() {
        return key;
    }
}
