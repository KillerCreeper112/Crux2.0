package killercreepr.cruxattributes.api.attribute;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface DynamicCruxAttributeInstance extends CruxAttributeInstance {
    boolean removeModifiers(@NotNull Key... path);
    void addModifiers(@NotNull CruxAttributeModifier... modifiers);
}
