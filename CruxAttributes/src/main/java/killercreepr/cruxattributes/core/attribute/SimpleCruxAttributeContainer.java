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
    private final Map<CruxAttribute, CruxAttributeInstance> attributes = new HashMap<>();
    public SimpleCruxAttributeContainer(@NotNull CruxAttributeInstance... attributes){
        for(CruxAttributeInstance i : attributes) {
            this.attributes.put(i.getAttribute(), i);
        }
    }

    public SimpleCruxAttributeContainer(@NotNull Collection<CruxAttributeInstance> attributes){
        for(CruxAttributeInstance i : attributes) {
            this.attributes.put(i.getAttribute(), i);
        }
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
}
