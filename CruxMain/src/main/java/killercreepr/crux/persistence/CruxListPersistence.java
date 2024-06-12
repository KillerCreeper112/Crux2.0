package killercreepr.crux.persistence;

import killercreepr.crux.persistence.impl.ListTagType;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;

public class CruxListPersistence {
    public final ListTagType<java.util.UUID> UUID = new ListTagType<>(CruxPersistence.UUID);
    public final ListTagType<PotionEffect> POTION_EFFECT = new ListTagType<>(CruxPersistence.POTION_EFFECT);
    public final ListTagType<Location> LOCATION = new ListTagType<>(CruxPersistence.LOCATION);
}
