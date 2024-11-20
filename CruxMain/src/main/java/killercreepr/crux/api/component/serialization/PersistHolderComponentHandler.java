package killercreepr.crux.api.component.serialization;

import killercreepr.crux.api.persistence.PersistenceComponentHandler;
import killercreepr.crux.core.persistence.CruxPersist;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PersistHolderComponentHandler extends PersistenceComponentHandler {
    @Nullable
    PersistentDataHolder getComponentsPersistentHolder();

    @Override
    default @Nullable PersistentDataContainer getComponentsPersistentContainer() {
        PersistentDataHolder item = getComponentsPersistentHolder();
        if(item == null) return null;
        PersistentDataContainer container = CruxPersist.COMPONENTS.get(item, null);
        if(container == null) container = item.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
        return container;
    }

    @Override
    default void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        PersistentDataHolder item = getComponentsPersistentHolder();
        CruxPersist.COMPONENTS.set(item, data.isEmpty() ? null : data);
    }
}
