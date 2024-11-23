package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeInstance;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxAttributeInstance {
    static CruxAttributeInstance instance(@NotNull CruxAttribute attribute, @NotNull Collection<CruxAttributeModifier> modifiers){
        return new SimpleCruxAttributeInstance(attribute, modifiers);
    }

    static CruxAttributeInstance instance(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier... modifiers){
        return new SimpleCruxAttributeInstance(attribute, modifiers);
    }

    double getValue(@NotNull CruxAttribute.Operation operation);
    double getValue();

    double getBaseValue();

    @NotNull CruxAttribute getAttribute();

    @Nullable
    CruxAttributeModifier getModifier(@NotNull NamespacedKey key);

    @NotNull Collection<CruxAttributeModifier> getModifiers();
}
