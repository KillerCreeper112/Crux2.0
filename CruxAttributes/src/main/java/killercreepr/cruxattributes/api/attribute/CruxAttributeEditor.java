package killercreepr.cruxattributes.api.attribute;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface CruxAttributeEditor extends CruxAttributeAccessor{
    void addModifier(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier modifier);
    void removeModifier(@NotNull CruxAttribute attribute, @NotNull Key... path);
    void clearModifiers(@NotNull CruxAttribute attribute);
    @Contract(pure = true)
    @NotNull CruxAttributeEditor copy();
}
