package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.*;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
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
    public CruxAttributeEditor addModifier(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier modifier) {
        instances.computeIfAbsent(attribute, (d) -> CruxAttributeInstance.dynamicInstance(attribute)).addModifiers(modifier);
        return this;
    }

    @Override
    public CruxAttributeEditor removeModifier(@NotNull CruxAttribute attribute, @NotNull Key... path) {
        if(path == null || path.length < 1) throw new IllegalStateException("No path has been provided to remove CruxAttributeModifiers!");
        DynamicCruxAttributeInstance instance = getInstance(attribute);
        if(instance == null) return this;
        instance.removeModifiers(path);
        if(instance.isEmpty()) clearModifiers(attribute);
        return this;
    }

    @Override
    public CruxAttributeEditor removeModifiers(@NotNull Key @NotNull ... path) {
        if(path.length < 1) throw new IllegalStateException("No path has been provided to remove CruxAttributeModifiers!");
        instances.values().removeIf(instance -> {
            instance.removeModifiers(path);
            return instance.isEmpty();
        });
        return this;
    }

    @Override
    public CruxAttributeEditor clearModifiers(@NotNull CruxAttribute attribute) {
        instances.remove(attribute);
        return this;
    }

    @Override
    public CruxAttributeEditor addAllModifiers(@NotNull CruxAttributeAccessor attributes) {
        attributes.getInstances().forEach(this::addAllModifiers);
        return this;
    }

    @Override
    public CruxAttributeEditor addAllModifiers(@NotNull CruxAttributeInstance instance) {
        instance.getModifiers().forEach(mod -> addModifier(instance.getAttribute(), mod));
        return this;
    }

    @Override
    public CruxAttributeEditor clearAllModifiers() {
        instances.clear();
        return this;
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
    public @NotNull CruxAttributeHandler copyToHandler(@NotNull CruxAttributeHandler handler) {
        getInstances().forEach(handler::addAllModifiers);
        return handler;
    }

    @Override
    public @NotNull CruxAttributeHandler copyToNewHandler() {
        return copy();
    }

    @Override
    public @NotNull CruxAttributeHandler copy() {
        Map<CruxAttribute, DynamicCruxAttributeInstance> map = new HashMap<>();
        for (DynamicCruxAttributeInstance value : instances.values()) {
            map.put(value.getAttribute(), value.copy());
        }
        return new SimpleCruxAttributeHandler(map);
    }

    public static class Builder implements CruxAttributeHandler.Builder{
        protected final CruxAttributeHandler handler = CruxAttributeHandler.attributeHandler();
        @Override
        public CruxAttributeHandler.Builder add(CruxAttribute attribute, CruxAttributeModifier... modifiers) {
            for(CruxAttributeModifier m : modifiers){
                handler.addModifier(attribute, m);
            }
            return this;
        }

        @Override
        public CruxAttributeHandler.Builder add(CruxAttribute attribute, Collection<CruxAttributeModifier> modifiers) {
            for(CruxAttributeModifier m : modifiers){
                handler.addModifier(attribute, m);
            }
            return this;
        }

        @Override
        public CruxAttributeHandler.Builder add(CruxAttributeInstance instance) {
            return add(instance.getAttribute(), instance.getModifiers());
        }

        @Override
        public CruxAttributeHandler.Builder addAll(Collection<CruxAttributeInstance> instance) {
            for (CruxAttributeInstance i : instance) {
                add(i);
            }
            return this;
        }

        @Override
        public CruxAttributeHandler build() {
            return handler;
        }

        @Override
        public CruxAttributeAccessor buildImmutable() {
            Map<CruxAttribute, CruxAttributeInstance> map = new HashMap<>();
            for (CruxAttributeInstance instance : handler.getInstances()) {
                if(instance instanceof DynamicCruxAttributeInstance i){
                    instance = CruxAttributeInstance.instance(i);
                }
                map.put(instance.getAttribute(), instance);
            }
            return CruxAttributeAccessor.attributeAccessor(map);
        }
    }
}
