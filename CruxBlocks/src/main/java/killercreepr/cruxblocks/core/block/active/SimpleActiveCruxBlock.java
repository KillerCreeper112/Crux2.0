package killercreepr.cruxblocks.core.block.active;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.*;
import killercreepr.cruxblocks.api.block.component.*;
import killercreepr.cruxblocks.api.item.KeyedItemProvider;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.CruxBlocksModule;
import killercreepr.cruxblocks.core.mining.user.EntityMiner;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class SimpleActiveCruxBlock implements ActiveCruxBlock, ActiveCruxInteractable, ActiveCruxRedstonePowerable,
    ActiveCruxEntityPhysicalInteract, ActiveCruxEntityMoveInside {
    protected final @NotNull Block block;
    protected final @NotNull CruxBlock cruxBlock;

    public SimpleActiveCruxBlock(@NotNull Block block, @NotNull CruxBlock cruxBlock) {
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
                switch (handle) {
                    case Player player -> builder.putAll(handle, "entity", "player");
                    case Entity entity -> builder.putAll(handle, "entity");
                    case ItemStack itemStack -> builder.putAll(handle, "item");
                    default -> {}
                }
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

    @Override
    public @Nullable Event.Result interact(@NotNull PlayerInteractEvent event) {
        for(CruxInteractableBlockComponent comp : getCruxBlock().getComponents().getAllOfType(CruxInteractableBlockComponent.class)){
            var result = comp.interact(this, event);
            if(result != null) return result;
        }
        return null;
    }

    @Override
    public Float onMine(@Nullable Miner miner) {
        for(var comp : getCruxBlock().getComponents().getAllOfType(CruxMinerMineComponent.class)){
            Float got = comp.onMinerMine(miner, this);
            if(got != null) return got;
        }
        return ActiveCruxBlock.super.onMine(miner);
    }

    @Override
    public boolean isRedstonePowerable() {
        return getCruxBlock().getComponents().hasAnyOfType(CruxRedstonePowerableComponent.class);
    }

    @Override
    public void redstonePowerChanged(Block from, int newPower) {
        for(CruxRedstonePowerableComponent comp : getCruxBlock().getComponents().getAllOfType(CruxRedstonePowerableComponent.class)){
            comp.redstonePowerChanged(from, newPower);
        }
    }

    @Override
    public void onEntityPhysicalInteract(@NotNull Entity e, @NotNull PlayerInteractEvent event) {
        for(CruxInteractablePhysicalBlockComponent comp : getCruxBlock().getComponents().getAllOfType(CruxInteractablePhysicalBlockComponent.class)){
            comp.onEntityPhysicalInteract(e, event);
        }
    }

    @Override
    public void onEntityPhysicalInteract(@NotNull Entity e, @NotNull EntityInteractEvent event) {
        for(CruxInteractablePhysicalBlockComponent comp : getCruxBlock().getComponents().getAllOfType(CruxInteractablePhysicalBlockComponent.class)){
            comp.onEntityPhysicalInteract(e, event);
        }
    }

    @Override
    public void onEntityMoveInside(@NotNull Entity e) {
        for(CruxEntityMoveInsideBlockComponent comp : getCruxBlock().getComponents().getAllOfType(CruxEntityMoveInsideBlockComponent.class)){
            comp.onEntityMoveInside(e);
        }
    }
}
