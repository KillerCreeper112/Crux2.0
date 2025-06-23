package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public class SimpleEntityTickablesContainer implements EntityTickablesContainer {
    protected final Collection<EntityTickableModifier> modifiers;

    public SimpleEntityTickablesContainer(Collection<EntityTickableModifier> modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SimpleEntityTickablesContainer that)) return false;
        return Objects.equals(modifiers, that.modifiers);
    }

    @Override
    public String toString() {
        return "SimpleEntityTickablesContainer{" +
            "modifiers=" + modifiers +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(modifiers);
    }

    @Override
    public @NotNull Collection<EntityTickableModifier> getTickableModifiers() {
        return modifiers;
    }
}
