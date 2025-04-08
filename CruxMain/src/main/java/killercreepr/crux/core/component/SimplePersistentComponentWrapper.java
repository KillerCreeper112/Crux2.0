package killercreepr.crux.core.component;

import killercreepr.crux.api.persistence.PersistenceComponentHandler;
import killercreepr.crux.core.persistence.CruxPersist;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimplePersistentComponentWrapper implements PersistenceComponentHandler {
    protected final PersistentDataContainer container;

    public SimplePersistentComponentWrapper(PersistentDataContainer container) {
        this.container = container;
    }

    @Override
    public @Nullable PersistentDataContainer getComponentsPersistentContainer() {
        PersistentDataContainer container = CruxPersist.COMPONENTS.get(this.container, null);
        if(container == null) container = this.container.getAdapterContext().newPersistentDataContainer();
        return container;
    }

    @Override
    public void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        CruxPersist.COMPONENTS.set(container, data.isEmpty() ? null : data);
    }

    @Override
    public void clearComponents() {
        CruxPersist.COMPONENTS.set(container, null);
    }
}
