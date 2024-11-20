package killercreepr.cruxblocks.api.block.active;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.core.Crux;
import killercreepr.cruxblocks.core.block.data.CustomBlockData;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

public interface ActiveCruxTickedBlock extends ActiveCruxBlock, ManagedTicked {
    NamespacedKey CUSTOM_TICKED_KEY = Crux.key("custom_ticked");
    @Override
    default void started() {
        saveState();
    }

    default void saveState(){
        CustomBlockData data = CustomBlockData.wrap(getBlock());
        data.set(CUSTOM_TICKED_KEY, PersistentDataType.BOOLEAN, true);
    }
}
