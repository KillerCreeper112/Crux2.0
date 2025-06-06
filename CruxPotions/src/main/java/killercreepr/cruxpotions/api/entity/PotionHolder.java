package killercreepr.cruxpotions.api.entity;

import killercreepr.cruxpotions.api.event.EntityCruxPotionEvent;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PotionHolder {
    @NotNull Collection<ActivePotion> getActiveEffects();
    boolean hasPotion(Key key);
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
