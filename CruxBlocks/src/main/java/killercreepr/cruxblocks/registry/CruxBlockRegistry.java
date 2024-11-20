package killercreepr.cruxblocks.registry;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxBlockRegistry extends KeyedRegistry<CruxBlock> {
    @Nullable CruxBlock getByTexture(@Nullable TextureData textureData);

    @NotNull Collection<CruxBlock> getPossibleBlocks(@Nullable TextureData data);

    @Nullable CruxBlock getByBlock(@NotNull Block block);

    @Nullable CruxBlock getByBlockState(@NotNull BlockState state);
    @Nullable CruxBlock getByBlockData(@NotNull Block block, @NotNull BlockData data);

    @Nullable CruxBlock getByBlockData(@NotNull BlockData data);

    @Nullable CruxBlockGroup getGroup(@NotNull Key key);

    <T extends CruxBlockGroup> T registerGroup(@NotNull T group);
    CruxBlockGroup unregisterGroup(@NotNull Key key);
    KeyedRegistry<CruxBlockGroup> getGroups();
}
