package killercreepr.cruxpotions.api.entity;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxpotions.api.event.EntityCruxPotionEvent;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PotionHolder {
    static PotionHolder potionHolderOrCreate(Entity e){
        return EntityMemory.getOrCreateDataHolder(e, SimplePotionHolder.class, SimplePotionHolder::new);
    }
    static PotionHolder potionHolder(Entity e){
        return EntityMemory.getDataHolder(e, SimplePotionHolder.class);
    }

    @NotNull Collection<ActivePotion> getActiveEffects();
    boolean hasPotion(Key key);
    boolean hasPotion(CruxPotion type);
    @NotNull EntityCruxPotionEvent addPotion(@NotNull ActivePotion potion);
    @NotNull EntityCruxPotionEvent addPotion(@NotNull ActivePotion potion, boolean override);
    @NotNull EntityCruxPotionEvent addPotion(@NotNull ActivePotion potion, boolean override, boolean skipEventCall);
    @Nullable ActivePotion getPotion(@NotNull CruxPotion type);
    void setPotions(@Nullable Collection<ActivePotion> effects);
    boolean removePotionCheck(@NotNull CruxPotion type);
    @Nullable EntityCruxPotionEvent removePotion(@NotNull CruxPotion type);
    @Nullable EntityCruxPotionEvent removePotion(@NotNull CruxPotion type, boolean skipEventCall);
    int clearPotions();
    @NotNull Collection<EntityCruxPotionEvent> clearPotions(boolean skipEventCall);
    int getPotionDuration(@NotNull CruxPotion type);
    int getPotionAmplifier(@NotNull CruxPotion type);
}
