package killercreepr.cruxblocks.block.active;

import killercreepr.crux.Crux;
import killercreepr.crux.data.tick.ManagedTicked;
import killercreepr.cruxblocks.block.data.CustomBlockData;
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
