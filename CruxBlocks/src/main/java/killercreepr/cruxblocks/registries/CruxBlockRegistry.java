package killercreepr.cruxblocks.registries;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxBlockRegistry extends SimpleKeyedRegistry<CruxBlock> {
    protected final KeyedRegistry<CruxBlockGroup> GROUPS = new SimpleKeyedRegistry<>();
    protected final MappedRegistry<TextureData, CruxBlock> TEXTURE_DATA_TO_BLOCK = new SimpleMappedRegistry<>();

    @Contract("null -> null")
    public @Nullable CruxBlock getByTexture(@Nullable TextureData textureData){
        if(textureData==null) return null;
        return TEXTURE_DATA_TO_BLOCK.get(textureData);
    }

    public @Nullable CruxBlock getByBlock(@NotNull Block block){
        return getByTexture(TextureData.buildFromBlock(block));
    }

    public @Nullable CruxBlock getByBlockData(@NotNull BlockData data){
        return getByTexture(TextureData.buildFromBlockData(data));
    }

    public @Nullable CruxBlockGroup getGroup(@NotNull Key key){
        return GROUPS.get(key);
    }

    public <T extends CruxBlockGroup> T registerGroup(@NotNull T group){
        GROUPS.register(group);
        group.forEach(this::register);
        return group;
    }

    @Override
    public @NotNull CruxBlock register(@NotNull CruxBlock object) {
        TEXTURE_DATA_TO_BLOCK.register(object.getTextureData(), object);
        return super.register(object);
    }

    @Override
    public boolean unregister(@NotNull CruxBlock object) {
        TEXTURE_DATA_TO_BLOCK.remove(object.getTextureData());
        return super.unregister(object);
    }

    @Override
    public @NotNull CruxBlock register(@NotNull Key key, @NotNull CruxBlock value) {
        TEXTURE_DATA_TO_BLOCK.register(value.getTextureData(), value);
        return super.register(key, value);
    }

    @Override
    public @Nullable CruxBlock remove(@NotNull Key key) {
        CruxBlock block = super.remove(key);
        if(block != null) TEXTURE_DATA_TO_BLOCK.register(block.getTextureData(), block);
        return block;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull CruxBlock value) {
        TEXTURE_DATA_TO_BLOCK.register(value.getTextureData(), value);
        return super.remove(key, value);
    }

    public KeyedRegistry<CruxBlockGroup> getGroups() {
        return GROUPS;
    }

    public MappedRegistry<TextureData, CruxBlock> getTextureDataToBlock() {
        return TEXTURE_DATA_TO_BLOCK;
    }
}
