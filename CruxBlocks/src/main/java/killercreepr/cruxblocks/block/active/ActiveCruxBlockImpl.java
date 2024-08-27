package killercreepr.cruxblocks.block.active;

import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.CruxBlocksModule;
import killercreepr.cruxblocks.block.CruxBlock;
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

public class ActiveCruxBlockImpl implements ActiveCruxBlock{
    protected final @NotNull Block block;
    protected final @NotNull CruxBlock cruxBlock;

    public ActiveCruxBlockImpl(@NotNull Block block, @NotNull CruxBlock cruxBlock) {
        this.block = block;
        this.cruxBlock = cruxBlock;
    }

    @Override
    public @Nullable Collection<ItemStack> getDrops(@Nullable Miner miner) {
        CruxBlocksModule module = CruxRegistries.MODULES.getModuleOrThrow(CruxBlocksModule.class);
        KeyedItemProvider provider = module.getKeyedItemProvider();
        if(provider==null) return ActiveCruxBlock.super.getDrops(miner);
        Key key = cruxBlock.getGroup() == null ? cruxBlock.key() : cruxBlock.getGroup().key();
        Entity entity;
        if(miner instanceof EntityMiner d) entity = d.getEntity();
        else entity = null;

        ItemStack item = provider.get(key, entity);
        if(item == null) return ActiveCruxBlock.super.getDrops(miner);
        return Set.of(item);
    }

    @Override
    public @NotNull Block getBlock() {
        return block;
    }

    @Override
    public @NotNull CruxBlock getCruxBlock() {
        return cruxBlock;
    }

    @Override
    public boolean isValid() {
        return cruxBlock.getTextureData().compareTexture(block);
    }
}
