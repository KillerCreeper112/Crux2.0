package killercreepr.cruxblocks.block.active;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.CruxBlocksModule;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.item.KeyedItemProvider;
import killercreepr.cruxblocks.user.EntityMiner;
import killercreepr.cruxblocks.user.Miner;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

    public @NotNull Key buildLootTableKey(){
        Key key = cruxBlock.getGroup() == null ? cruxBlock.key() : cruxBlock.getGroup().key();
        return Key.key(key.namespace(), "block/" + key.value());
    }

    public @Nullable LootTable<ItemStack> getLootTable(){
        return CruxRegistries.ITEM_LOOT_TABLE.get(buildLootTableKey());
    }

    public @NotNull LootContext buildLootContext(@Nullable Miner miner){
        DataExchange.Builder builder = DataExchange.builder()
            .putAll(block, "block", "block_broken")
            ;
        if(miner != null){
            Object handle = miner.getHandle();
            if(handle != null){
                builder.putAll(handle, "miner");
                if(handle instanceof Player) builder.putAll(handle, "entity", "player");
                else if(handle instanceof Entity) builder.putAll(handle, "entity");
                else if(handle instanceof ItemStack) builder.putAll(handle, "item");
            }
        }
        return LootContext.builder()
            .info(builder.build())
            .location(block.getLocation())
            .looter(miner == null ? null : miner.getHandle())
            .looted(block)
            .build();
    }

    @Override
    public @Nullable Collection<ItemStack> getDrops(@Nullable Miner miner) {
        LootTable<ItemStack> lootTable = getLootTable();
        if(lootTable != null){
            return lootTable.populateLoot(buildLootContext(miner));
        }

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
