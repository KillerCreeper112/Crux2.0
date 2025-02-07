package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxtickables.api.entity.tickable.EntityTickableInstance;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class SimpleEntityTickablesContainer implements EntityTickablesContainer {
    protected final Collection<EntityTickableInstance> instances;

    public SimpleEntityTickablesContainer(Collection<EntityTickableInstance> instances) {
        this.instances = instances;
    }

    @Override
    public @NotNull Collection<EntityTickableInstance> getTickableInstances() {
        return instances;
    }
}
