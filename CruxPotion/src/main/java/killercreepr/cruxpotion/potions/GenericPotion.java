package killercreepr.cruxpotion.potions;

import killercreepr.crux.util.CruxString;
import killercreepr.cruxpotion.CruxPotion;
import killercreepr.cruxpotion.potions.inflictor.PotionInflictor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class GenericPotion implements CustomPotion {
    protected final NamespacedKey key;

    public GenericPotion(@NotNull NamespacedKey key) {
        this.key = key;
    }

    @Override
    public @NotNull String getName(){
        return CruxString.toTitleCase(key.getKey());
    }

    protected @NotNull PotionEffectType.Category defaultCategory(){ return PotionEffectType.Category.NEUTRAL; }

    @Override
    public @NotNull PotionEffectType.Category getCategory(){
        Map<NamespacedKey, PotionEffectType.Category> value = CruxPotion.cfg().POTION_CATEGORIES.value();
        if(value == null) return defaultCategory();
        return value.getOrDefault(key, defaultCategory());
    }
    @Override
    public abstract @NotNull ActivePotion create(@NotNull Entity e, int duration, int amplifier, @Nullable PotionInflictor inflictor);

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }
}
