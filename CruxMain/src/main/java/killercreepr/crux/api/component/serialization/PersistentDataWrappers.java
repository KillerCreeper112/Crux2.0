package killercreepr.crux.api.component.serialization;

import killercreepr.crux.core.component.SimpleBlockComponentWrapper;
import killercreepr.crux.core.component.SimplePersistentDataComponentWrapper;
import org.bukkit.block.BlockState;
import org.bukkit.persistence.PersistentDataHolder;

public interface PersistentDataWrappers {
    static PersistHolderComponentHandler wrapBlockState(BlockState block){
        return new SimpleBlockComponentWrapper(block);
    }

    static PersistHolderComponentHandler wrapPersistentHolder(PersistentDataHolder holder){
        return new SimplePersistentDataComponentWrapper(holder);
    }
}
