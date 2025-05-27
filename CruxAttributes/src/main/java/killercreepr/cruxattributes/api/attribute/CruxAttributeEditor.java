package killercreepr.cruxattributes.api.attribute;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CruxAttributeEditor extends CruxAttributeAccessor{
    CruxAttributeEditor addModifier(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier modifier);
    CruxAttributeEditor removeModifier(@NotNull CruxAttribute attribute, @NotNull Key... path);
    CruxAttributeEditor removeModifiers(@NotNull Key @NotNull... path);
    CruxAttributeEditor clearModifiers(@NotNull CruxAttribute attribute);
    CruxAttributeEditor addAllModifiers(@NotNull CruxAttributeAccessor attributes);
    CruxAttributeEditor addAllModifiers(@NotNull CruxAttributeInstance instance);
    CruxAttributeEditor clearAllModifiers();
    @Contract(pure = true)
    @NotNull CruxAttributeEditor copy();
}
