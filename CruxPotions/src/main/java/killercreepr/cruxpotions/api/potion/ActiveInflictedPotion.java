package killercreepr.cruxpotions.api.potion;

import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.jetbrains.annotations.Nullable;

public interface ActiveInflictedPotion {
    @Nullable PotionInflictor getInflictor();
    void setInflictor(@Nullable PotionInflictor inflictor);
}
