package killercreepr.crux.core.component;

import killercreepr.crux.api.component.serialization.PersistHolderComponentHandler;
import killercreepr.crux.core.persistence.CruxPersist;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimplePersistentDataComponentWrapper implements PersistHolderComponentHandler {
    protected final PersistentDataHolder holder;
    public SimplePersistentDataComponentWrapper(PersistentDataHolder holder) {
        this.holder = holder;
    }

    @Override
    public @Nullable PersistentDataHolder getComponentsPersistentHolder() {
        return holder;
    }

    @Override
    public void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        PersistentDataHolder item = getComponentsPersistentHolder();
        CruxPersist.COMPONENTS.set(item, data.isEmpty() ? null : data);
    }

    @Override
    public void clearComponents() {
        CruxPersist.COMPONENTS.set(holder, null);
    }
}
