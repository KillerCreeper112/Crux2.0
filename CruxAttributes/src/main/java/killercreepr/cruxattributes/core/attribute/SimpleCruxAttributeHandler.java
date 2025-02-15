package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.*;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleCruxAttributeHandler implements CruxAttributeHandler {
    protected final Map<CruxAttribute, DynamicCruxAttributeInstance> instances = new HashMap<>();

    public SimpleCruxAttributeHandler(Collection<DynamicCruxAttributeInstance> instances) {
        for(DynamicCruxAttributeInstance i : instances){
            this.instances.put(i.getAttribute(), i);
        }
    }

    public SimpleCruxAttributeHandler(Map<CruxAttribute, DynamicCruxAttributeInstance> map) {
        instances.putAll(map);
    }

    public SimpleCruxAttributeHandler() {
    }

    @Override
    public void addModifier(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier modifier) {
        instances.computeIfAbsent(attribute, (d) -> CruxAttributeInstance.dynamicInstance(attribute)).addModifiers(modifier);
    }

    @Override
    public void removeModifier(@NotNull CruxAttribute attribute, @NotNull Key... path) {
        if(path == null || path.length < 1) throw new IllegalStateException("No path has been provided to remove CruxAttributeModifiers!");
        DynamicCruxAttributeInstance instance = getInstance(attribute);
        if(instance == null) return;
        instance.removeModifiers(path);
        if(instance.isEmpty()) clearModifiers(attribute);
    }

    @Override
    public void clearModifiers(@NotNull CruxAttribute attribute) {
        instances.remove(attribute);
    }

    @Override
    public double getValue(@NotNull CruxAttribute attribute) {
        return getValueOrDefault(attribute, 0D);
    }

    @Override
    public double getValueOrDefault(@NotNull CruxAttribute attribute, double fallback) {
        CruxAttributeInstance instance = getInstance(attribute);
        return instance == null ? fallback : instance.getValue();
    }

    @Override
    public @Nullable DynamicCruxAttributeInstance getInstance(@NotNull CruxAttribute attribute) {
        return instances.get(attribute);
    }

    @Override
    public @NotNull Collection<? extends CruxAttributeInstance> getInstances() {
        return instances.values();
    }

    @Override
    public @NotNull CruxAttributeHandler copy() {
        Map<CruxAttribute, DynamicCruxAttributeInstance> map = new HashMap<>();
        for (DynamicCruxAttributeInstance value : instances.values()) {
            map.put(value.getAttribute(), value.copy());
        }
        return new SimpleCruxAttributeHandler(map);
    }
}
