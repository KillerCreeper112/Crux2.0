package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CruxAttributeHandler extends CruxAttributeAccessor, CruxAttributeEditor{
    static Builder builder(){
        return new SimpleCruxAttributeHandler.Builder();
    }

    static CruxAttributeHandler attributeHandler(){
        return new SimpleCruxAttributeHandler();
    }

    static CruxAttributeHandler attributeHandler(@NotNull Collection<DynamicCruxAttributeInstance> instances){
        return new SimpleCruxAttributeHandler(instances);
    }
    @Contract(pure = true)
    @NotNull CruxAttributeHandler copy();

    interface Builder{
        Builder add(CruxAttribute attribute, CruxAttributeModifier... modifiers);
        Builder add(CruxAttribute attribute, Collection<CruxAttributeModifier> modifiers);
        Builder add(CruxAttributeInstance instance);
        CruxAttributeHandler build();
        CruxAttributeAccessor buildImmutable();
    }
}
