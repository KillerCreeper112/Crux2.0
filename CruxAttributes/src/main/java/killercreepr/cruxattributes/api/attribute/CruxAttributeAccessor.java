package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeAccessor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface CruxAttributeAccessor {
    static CruxAttributeAccessor attributeAccessor(Map<CruxAttribute, CruxAttributeInstance> map){
        return new SimpleCruxAttributeAccessor(map);
    }
    static CruxAttributeAccessor attributeAccessor(Collection<CruxAttributeInstance> instances){
        Map<CruxAttribute, CruxAttributeInstance> map = new HashMap<>();
        for(CruxAttributeInstance i : instances){
            map.put(i.getAttribute(), i);
        }
        return attributeAccessor(map);
    }
    static CruxAttributeAccessor attributeAccessor(CruxAttributeInstance... instances){
        Map<CruxAttribute, CruxAttributeInstance> map = new HashMap<>();
        for(CruxAttributeInstance i : instances){
            map.put(i.getAttribute(), i);
        }
        return attributeAccessor(map);
    }

    double getValue(@NotNull CruxAttribute attribute);
    double getValueOrDefault(@NotNull CruxAttribute attribute, double fallback);
    @Nullable CruxAttributeInstance getInstance(@NotNull CruxAttribute attribute);
    @NotNull Collection<? extends CruxAttributeInstance> getInstances();
    @Contract(pure = true)
    @NotNull CruxAttributeHandler copyToHandler(@NotNull CruxAttributeHandler handler);
    @Contract(pure = true)
    @NotNull CruxAttributeHandler copyToNewHandler();
}
