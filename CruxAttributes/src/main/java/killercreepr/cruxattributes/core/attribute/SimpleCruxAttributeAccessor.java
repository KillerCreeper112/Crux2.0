package killercreepr.cruxattributes.core.attribute;

import com.google.common.collect.ImmutableMap;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeAccessor;
import killercreepr.cruxattributes.api.attribute.CruxAttributeHandler;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class SimpleCruxAttributeAccessor implements CruxAttributeAccessor {
    protected final Map<CruxAttribute, CruxAttributeInstance> instances;

    public SimpleCruxAttributeAccessor(Map<CruxAttribute, CruxAttributeInstance> map) {
        this.instances = ImmutableMap.copyOf(map);
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
    public @Nullable CruxAttributeInstance getInstance(@NotNull CruxAttribute attribute) {
        return instances.get(attribute);
    }

    @Override
    public @NotNull Collection<? extends CruxAttributeInstance> getInstances() {
        return instances.values();
    }

    @Override
    public @NotNull CruxAttributeHandler copyToHandler(@NotNull CruxAttributeHandler handler) {
        handler.addAllModifiers(this);
        return handler;
    }

    @Override
    public @NotNull CruxAttributeHandler copyToNewHandler() {
        return copyToHandler(CruxAttributeHandler.attributeHandler());
    }
}
