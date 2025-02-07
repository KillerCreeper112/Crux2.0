package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleCruxAttributeContainer implements CruxAttributeContainer {
    private final Map<CruxAttribute, CruxAttributeInstance> attributes;
    public SimpleCruxAttributeContainer(@NotNull CruxAttributeInstance... attributes){
        this(new HashMap<>());
        for(CruxAttributeInstance i : attributes) {
            this.attributes.put(i.getAttribute(), i);
        }
    }

    public SimpleCruxAttributeContainer(@NotNull Collection<CruxAttributeInstance> attributes){
        this(new HashMap<>());
        for(CruxAttributeInstance i : attributes) {
            this.attributes.put(i.getAttribute(), i);
        }
    }

    public SimpleCruxAttributeContainer(Map<CruxAttribute, CruxAttributeInstance> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "SimpleCruxAttributeContainer{attributes=" + attributes + "}";
    }

    @Override
    public @NotNull Collection<CruxAttributeInstance> getAttributeInstances() {
        return attributes.values();
    }

    public @Nullable CruxAttributeInstance getAttribute(@NotNull CruxAttribute attribute){
        return attributes.get(attribute);
    }

    public boolean hasAttribute(@NotNull CruxAttribute attribute){
        return attributes.containsKey(attribute);
    }

    @Override
    public double getValue(CruxAttribute attribute) {
        return getValue(attribute, 0D);
    }

    @Override
    public double getValue(CruxAttribute attribute, double fallBack) {
        CruxAttributeInstance instance = getAttribute(attribute);
        return instance == null ? fallBack : instance.getValue();
    }
}
