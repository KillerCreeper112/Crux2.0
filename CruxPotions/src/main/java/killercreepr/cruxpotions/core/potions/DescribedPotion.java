package killercreepr.cruxpotions.core.potions;

import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxString;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import killercreepr.cruxpotions.core.CruxPotionsModule;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class DescribedPotion implements CruxPotion {
    protected final @NotNull Key key;
    protected final @NotNull String description;
    public DescribedPotion(@NotNull Key key, @NotNull String description) {
        this.key = key;
        this.description = description;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public @NotNull String getName(){
        return CruxString.toTitleCase(key.value());
    }

    protected @NotNull PotionEffectType.Category defaultCategory(){ return PotionEffectType.Category.NEUTRAL; }

    @Override
    public @NotNull PotionEffectType.Category getCategory(){
        CruxPotionsModule module = CruxRegistries.MODULES.getModule(CruxPotionsModule.class);
        if(module==null) return defaultCategory();
        Map<Key, PotionEffectType.Category> value = module.values().potionCategories().value();
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
