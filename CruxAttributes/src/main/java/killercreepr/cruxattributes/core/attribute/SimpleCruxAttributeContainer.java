package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SimpleCruxAttributeContainer implements CruxAttributeContainer {
    private final Set<CruxAttributeInstance> attributes = new HashSet<>();
    public SimpleCruxAttributeContainer(@NotNull CruxAttributeInstance... attributes){
        this.attributes.addAll(Arrays.asList(attributes));
    }

    public SimpleCruxAttributeContainer(@NotNull Collection<CruxAttributeInstance> attributes){
        this.attributes.addAll(attributes);
    }

    public @Nullable CruxAttributeInstance getAttribute(@NotNull CruxAttribute attribute){
        for(CruxAttributeInstance i : attributes){
            if(i.getAttribute() == attribute) return i;
        }
        return null;
    }

    public boolean hasAttribute(@NotNull CruxAttribute attribute){
        for(CruxAttributeInstance i : attributes){
            if(i.getAttribute() == attribute) return true;
        }
        return false;
    }
}
