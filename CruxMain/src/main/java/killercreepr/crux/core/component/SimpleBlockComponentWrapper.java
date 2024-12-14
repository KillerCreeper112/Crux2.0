package killercreepr.crux.core.component;

import killercreepr.crux.api.component.serialization.PersistHolderComponentHandler;
import killercreepr.crux.core.persistence.CruxPersist;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleBlockComponentWrapper implements PersistHolderComponentHandler {
    protected final BlockState block;
    public SimpleBlockComponentWrapper(BlockState block) {
        this.block = block;
    }

    @Override
    public @Nullable PersistentDataHolder getComponentsPersistentHolder() {
        if(!(block instanceof PersistentDataHolder holder)) return null;
        return holder;
    }

    @Override
    public void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        PersistentDataHolder item = getComponentsPersistentHolder();
        CruxPersist.COMPONENTS.set(item, data.isEmpty() ? null : data);
        block.update();
    }

    @Override
    public void clearComponents() {
        if(!(block instanceof PersistentDataHolder holder)) return;
        CruxPersist.COMPONENTS.set(holder, null);
    }
}
