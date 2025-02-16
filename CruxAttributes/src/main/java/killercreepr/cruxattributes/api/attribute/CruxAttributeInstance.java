package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeInstance;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface CruxAttributeInstance {
    static CruxAttributeInstance instance(@NotNull CruxAttribute attribute, @NotNull Collection<CruxAttributeModifier> modifiers){
        return new SimpleCruxAttributeInstance(attribute, modifiers);
    }

    static CruxAttributeInstance instance(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier... modifiers){
        return new SimpleCruxAttributeInstance(attribute, modifiers);
    }

    static CruxAttributeInstance instance(@NotNull CruxAttributeInstance instance){
        return instance(instance.getAttribute(), instance.getModifiers());
    }

    static DynamicCruxAttributeInstance dynamicInstance(@NotNull CruxAttribute attribute, @NotNull Collection<CruxAttributeModifier> modifiers){
        return new SimpleCruxAttributeInstance.Dynamic(attribute, modifiers);
    }

    static DynamicCruxAttributeInstance dynamicInstance(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier... modifiers){
        return new SimpleCruxAttributeInstance.Dynamic(attribute, modifiers);
    }

    boolean isEmpty();

    double getValue(@NotNull CruxAttribute.Operation operation);
    double getValue();

    double getBaseValue();

    @NotNull CruxAttribute getAttribute();

    @Nullable
    CruxAttributeModifier getModifier(@NotNull NamespacedKey key);
    @NotNull List<CruxAttributeModifier> getModifiers(@NotNull CruxAttribute attribute, @NotNull Key... path);

    @NotNull Collection<CruxAttributeModifier> getModifiers();
}
