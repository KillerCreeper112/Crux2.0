package killercreepr.cruxtickables.api.entity.tickable;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface EntityTickableInstance {
    static EntityTickableInstance instance(EntityTickable tickable, Collection<EntityTickableModifier> modifiers){
        return new EntityTickableInstance() {
            @Override
            public @NotNull EntityTickable getTickable() {
                return tickable;
            }

            @Override
            public @Nullable EntityTickableModifier getModifier(@NotNull NamespacedKey key) {
                return null;
            }

            @Override
            public @NotNull Collection<EntityTickableModifier> getModifiers() {
                return modifiers;
            }
        };
    }

    @NotNull EntityTickable getTickable();

    @Nullable
    EntityTickableModifier getModifier(@NotNull NamespacedKey key);

    @NotNull Collection<EntityTickableModifier> getModifiers();
}
