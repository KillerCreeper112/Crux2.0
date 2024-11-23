package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxAttributeContainer {
    static CruxAttributeContainer container(@NotNull Collection<CruxAttributeInstance> attributes){
        return new SimpleCruxAttributeContainer(attributes);
    }

    static CruxAttributeContainer container(@NotNull CruxAttributeInstance... attributes){
        return new SimpleCruxAttributeContainer(attributes);
    }

    @Nullable CruxAttributeInstance getAttribute(@NotNull CruxAttribute attribute);
    boolean hasAttribute(@NotNull CruxAttribute attribute);
}
