package killercreepr.cruxpotions.api.potion.inflictor;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that can inflict custom potion effects onto an entity.
 */
public interface PotionInflictor {
    /**
     * Used for file serialization
     */
    @NotNull String getTypeID();
}
