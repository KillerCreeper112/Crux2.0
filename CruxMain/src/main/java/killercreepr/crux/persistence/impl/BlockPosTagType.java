package killercreepr.crux.persistence.impl;

import killercreepr.crux.Crux;
import killercreepr.crux.data.BlockPos;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BlockPosTagType implements PersistentDataType<PersistentDataContainer, BlockPos> {
    public static NamespacedKey k(@NotNull String name){ return Crux.key(name); }
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<BlockPos> getComplexType() {
        return BlockPos.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull BlockPos complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        c.set(k("x"), PersistentDataType.INTEGER, complex.x());
        c.set(k("y"), PersistentDataType.INTEGER, complex.y());
        c.set(k("z"), PersistentDataType.INTEGER, complex.z());
        return c;
    }

    @Override
    public @NotNull BlockPos fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        return new BlockPos(
            c.getOrDefault(k("x"), PersistentDataType.INTEGER, 0),
            c.getOrDefault(k("y"), PersistentDataType.INTEGER, 0),
            c.getOrDefault(k("z"), PersistentDataType.INTEGER, 0)
        );
    }
}