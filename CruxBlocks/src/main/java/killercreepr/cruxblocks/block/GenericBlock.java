package killercreepr.cruxblocks.block;

import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.CruxBlocksModule;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.item.KeyedItemProvider;
import killercreepr.cruxblocks.user.EntityMiner;
import killercreepr.cruxblocks.user.Miner;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class GenericBlock implements CruxBlock {
    protected final @NotNull Key key;
    protected final @NotNull TextureData textureData;
    protected @Nullable CruxBlockGroup group;
    public GenericBlock(@NotNull Key key, @NotNull TextureData textureData) {
        this.key = key;
        this.textureData = textureData;
    }

    @Override
    public @NotNull ActiveCruxBlock createActive(@NotNull Block block) {
        return new ActiveCruxBlockImpl(block, this){
            @Override
            public @Nullable Collection<ItemStack> getDrops(@Nullable Miner miner) {
                CruxBlocksModule module = CruxRegistries.MODULES.getModuleOrThrow(CruxBlocksModule.class);
                KeyedItemProvider provider = module.getKeyedItemProvider();
                if(provider==null) return super.getDrops(miner);
                Key key = group == null ? key() : group.key();
                Entity entity;
                if(miner instanceof EntityMiner d) entity = d.getEntity();
                else entity = null;

                ItemStack item = provider.get(key, entity);
                if(item == null) return super.getDrops(miner);
                return Set.of(item);
            }
        };
    }

    @Override
    public @Nullable CruxBlockGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(@Nullable CruxBlockGroup group) {
        this.group = group;
    }

    @Override
    public @NotNull TextureData getTextureData() {
        return textureData;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
