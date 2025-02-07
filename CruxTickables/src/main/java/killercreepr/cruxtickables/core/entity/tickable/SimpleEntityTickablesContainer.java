package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class SimpleEntityTickablesContainer implements EntityTickablesContainer {
    protected final Collection<EntityTickableModifier> modifiers;

    public SimpleEntityTickablesContainer(Collection<EntityTickableModifier> modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public @NotNull Collection<EntityTickableModifier> getTickableModifiers() {
        return modifiers;
    }
}
