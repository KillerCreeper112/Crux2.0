package killercreepr.cruxattributes.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistParser;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxAttributesPersistParser implements PersistParser<CruxAttributeContainer> {
    @Override
    public @NotNull CruxAttributeContainer decodeObject(@NotNull Object object) throws IllegalArgumentException {
        return null;
    }

    @Override
    public @NotNull Object encodeObject(@NotNull CruxAttributeContainer object) {
        return null;
    }

    @Override
    public @Nullable CruxAttributeContainer decode(@NotNull PersistentDataContainer from) {
        return null;
    }

    @Override
    public void encode(@NotNull PersistentDataContainer to, @Nullable CruxAttributeContainer value) {

    }

    @Override
    public @NotNull PersistentDataType<?, CruxAttributeContainer> dataType() {
        return null;
    }
}
