package killercreepr.crux.persistence;

import killercreepr.crux.item.ToolComponent;
import killercreepr.crux.persistence.impl.ListTagType;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;

public class CruxListPersistence {
    public final ListTagType<byte[], java.util.UUID> UUID = new ListTagType<>(CruxPersistence.UUID);
    public final ListTagType<PersistentDataContainer, PotionEffect> POTION_EFFECT = new ListTagType<>(CruxPersistence.POTION_EFFECT);
    public final ListTagType<PersistentDataContainer, Location> LOCATION = new ListTagType<>(CruxPersistence.LOCATION);
    public final ListTagType<PersistentDataContainer, ToolComponent> TOOL_COMPONENT = new ListTagType<>(CruxPersistence.TOOL_COMPONENT);
    public final ListTagType<String, Key> KEY = new ListTagType<>(CruxPersistence.CRUX_KEY);
}
