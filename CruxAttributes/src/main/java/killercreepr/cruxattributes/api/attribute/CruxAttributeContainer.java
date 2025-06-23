package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface CruxAttributeContainer extends CruxAttributeAccessor {
    static CruxAttributeContainer container(@NotNull Collection<CruxAttributeInstance> attributes){
        return new SimpleCruxAttributeContainer(attributes);
    }

    static CruxAttributeContainer container(@NotNull CruxAttributeInstance... attributes){
        return new SimpleCruxAttributeContainer(attributes);
    }

    static CruxAttributeContainer container(@NotNull Map<CruxAttribute, CruxAttributeInstance> map){
        return new SimpleCruxAttributeContainer(map);
    }

    static CruxAttributeContainer empty(){
        return EMPTY;
    }

    CruxAttributeContainer EMPTY = container(Map.of());

    @NotNull Collection<CruxAttributeInstance> getAttributeInstances();
    @Nullable CruxAttributeInstance getAttribute(@NotNull CruxAttribute attribute);
    boolean hasAttribute(@NotNull CruxAttribute attribute);

    double getValue(CruxAttribute attribute);
    double getValue(CruxAttribute attribute, double fallBack);
}
