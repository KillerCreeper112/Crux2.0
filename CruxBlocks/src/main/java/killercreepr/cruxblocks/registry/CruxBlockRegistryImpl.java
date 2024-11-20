package killercreepr.cruxblocks.registry;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.data.CustomBlockData;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistTags;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CruxBlockRegistryImpl extends SimpleKeyedRegistry<CruxBlock> implements CruxBlockRegistry {
    protected final KeyedRegistry<CruxBlockGroup> GROUPS = new SimpleKeyedRegistry<>();
    //protected final MappedRegistry<TextureData, CruxBlock> TEXTURE_DATA_TO_BLOCK = new SimpleMappedRegistry<>();
    protected final MappedRegistry<TextureData, Collection<CruxBlock>> TEXTURE_DATA_TO_BLOCK = new SimpleMappedRegistry<>();

    @Contract("null -> null")
    public @Nullable CruxBlock getByTexture(@Nullable TextureData textureData){
        if(textureData==null) return null;
        Collection<CruxBlock> blocks = TEXTURE_DATA_TO_BLOCK.get(textureData);
        if(blocks == null) return null;
        for(CruxBlock b : blocks){
            return b;
        }
        return null;
    }

    public @NotNull Collection<CruxBlock> getPossibleBlocks(@Nullable TextureData data){
        if(data==null) return Set.of();
        Collection<CruxBlock> blocks = TEXTURE_DATA_TO_BLOCK.get(data);
        if(blocks == null) return Set.of();
        return blocks;
    }

    public @Nullable CruxBlock getByBlock(@NotNull Block block){
        TextureData textureData = TextureData.buildFromBlock(block);
        Collection<CruxBlock> possible = getPossibleBlocks(textureData);
        if(possible.size() > 1){
            Key key = CruxBlocksPersistTags.CRUX_BLOCK_KEY.get(CustomBlockData.wrap(block), null);
            if(key != null) return get(key);
        }

        return getByTexture(textureData);
    }

    public @Nullable CruxBlock getByBlockState(@NotNull BlockState state){
        return getByBlockData(state.getBlock(), state.getBlockData());
    }

    public @Nullable CruxBlock getByBlockData(@NotNull Block block, @NotNull BlockData data){
        TextureData textureData = TextureData.buildFromBlockData(data);
        Collection<CruxBlock> possible = getPossibleBlocks(textureData);
        if(possible.size() > 1){
            Key key = CruxBlocksPersistTags.CRUX_BLOCK_KEY.get(CustomBlockData.wrap(block), null);
            if(key != null) return get(key);
            else{
                Crux.log(Level.WARNING, "CRUXBLOCK! " + block + " does not have a crux_block key set to it but the texture data possible amount is " + possible.size() + "!");
                for(CruxBlock b : possible){
                    Crux.log(Level.WARNING, "CRUXBLOCK: possible=" + b.key());
                }
                Crux.log(Level.WARNING, "^ CRUXBLOCK: texture=" + textureData);
            }
        }

        return getByTexture(TextureData.buildFromBlockData(data));
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
    public CruxBlockGroup unregisterGroup(@NotNull Key key) {
        CruxBlockGroup group = getGroup(key);
        if(group == null) return null;
        GROUPS.unregister(group);
        group.forEach(this::unregister);
        return group;
    }

    @Override
    public @NotNull CruxBlock register(@NotNull CruxBlock object) {
        TEXTURE_DATA_TO_BLOCK.computeIfAbsent(object.getTextureData(), (data) -> new HashSet<>()).add(object);
        //TEXTURE_DATA_TO_BLOCK.register(object.getTextureData(), object);
        return super.register(object);
    }

    @Override
    public boolean unregister(@NotNull CruxBlock object) {
        TEXTURE_DATA_TO_BLOCK.remove(object.getTextureData());
        return super.unregister(object);
    }

    @Override
    public @NotNull CruxBlock register(@NotNull Key key, @NotNull CruxBlock value) {
        TEXTURE_DATA_TO_BLOCK.computeIfAbsent(value.getTextureData(), (data) -> new HashSet<>()).add(value);
        //TEXTURE_DATA_TO_BLOCK.register(value.getTextureData(), value);
        return super.register(key, value);
    }

    @Override
    public @Nullable CruxBlock remove(@NotNull Key key) {
        CruxBlock block = super.remove(key);
        if(block != null){
            TEXTURE_DATA_TO_BLOCK.computeIfPresent(block.getTextureData(), (texture, blocks) ->{
                blocks.remove(block);
                return blocks.isEmpty() ? null : blocks;
            });
            //TEXTURE_DATA_TO_BLOCK.register(block.getTextureData(), block);
        }
        return block;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull CruxBlock value) {
        TEXTURE_DATA_TO_BLOCK.computeIfPresent(value.getTextureData(), (texture, blocks) ->{
            blocks.remove(value);
            return blocks.isEmpty() ? null : blocks;
        });
        //TEXTURE_DATA_TO_BLOCK.register(value.getTextureData(), value);
        return super.remove(key, value);
    }

    public KeyedRegistry<CruxBlockGroup> getGroups() {
        return GROUPS;
    }

    public MappedRegistry<TextureData, Collection<CruxBlock>> getTextureDataToBlock() {
        return TEXTURE_DATA_TO_BLOCK;
    }

    /*public MappedRegistry<TextureData, CruxBlock> getTextureDataToBlock() {
        return TEXTURE_DATA_TO_BLOCK;
    }*/
}
