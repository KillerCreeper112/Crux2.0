package killercreepr.cruxpotions.persistence;

import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.CruxPotion;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StoredPotion {
    @NotNull ActivePotion create(@NotNull Entity e);
    @NotNull ActivePotion create(@NotNull Entity e, @Nullable PotionInflictor inflictor);
    @NotNull CruxPotion getPotion();
    int getDuration();
    int getAmplifier();
}
