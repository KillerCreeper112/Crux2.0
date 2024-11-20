package killercreepr.cruxblocks.core.persistence.type;

import killercreepr.crux.core.Crux;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CruxBlockGroupTagType implements PersistentDataType<String, CruxBlockGroup> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<CruxBlockGroup> getComplexType() {
        return CruxBlockGroup.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull CruxBlockGroup complex, @NotNull PersistentDataAdapterContext context) {
        return complex.key().asString();
    }

    @Override
    public @NotNull CruxBlockGroup fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return Objects.requireNonNull(
            CruxBlocksRegistries.BLOCK.getGroup(Crux.key(primitive))
        );
    }
}
