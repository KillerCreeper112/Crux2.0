package killercreepr.cruxblocks.persistence;

import killercreepr.crux.Crux;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CruxBlockTagType implements PersistentDataType<String, CruxBlock> {
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<CruxBlock> getComplexType() {
        return CruxBlock.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull CruxBlock complex, @NotNull PersistentDataAdapterContext context) {
        return complex.key().asString();
    }

    @Override
    public @NotNull CruxBlock fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return Objects.requireNonNull(
            CruxBlocksRegistries.BLOCKS.get(Crux.key(primitive))
        );
    }
}
