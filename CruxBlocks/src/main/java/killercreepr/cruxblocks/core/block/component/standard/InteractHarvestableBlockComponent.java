package killercreepr.cruxblocks.core.block.component.standard;

import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.component.CruxInteractableBlockComponent;
import killercreepr.cruxblocks.api.mining.user.Miner;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InteractHarvestableBlockComponent implements CruxInteractableBlockComponent {
    protected final ItemPredicate item;
    protected final String interactType;
    protected final boolean breakBlock;
    protected final CruxBlockWrapper replaceWith;
    protected final CreateSound sound;
    protected final LootTable<ItemStack> lootTable;
    protected final int itemDamage;

    public InteractHarvestableBlockComponent(ItemPredicate item, String interactType, boolean breakBlock,
                                             CruxBlockWrapper replaceWith,
                                             CreateSound sound, LootTable<ItemStack> lootTable, int itemDamage) {
        this.item = item;
        this.interactType = interactType;
        this.breakBlock = breakBlock;
        this.replaceWith = replaceWith;
        this.sound = sound;
        this.lootTable = lootTable;
        this.itemDamage = itemDamage;
    }

    @Override
    public @Nullable Event.Result interact(@NotNull ActiveCruxBlock block, @NotNull PlayerInteractEvent event) {
        if("right_click".equalsIgnoreCase(interactType) && !event.getAction().isRightClick()) return null;
        if("left_click".equalsIgnoreCase(interactType) && !event.getAction().isLeftClick()) return null;
        if(item != null){
            ItemStack check = event.getItem();
            if(check == null) return null;
            if(!item.test(check)) return null;
        }
        Player p = event.getPlayer();
        if(breakBlock){
            var breakEvent = block.breakBlock(Miner.entity(event.getItem(), p, event.getHand()));
            if(breakEvent.isCancelled()) return null;
        }

        if(replaceWith != null){
            replaceWith.setBlock(block.getBlock());
        }

        if(lootTable != null){
            var ctx = LootContext.builder()
                .info(DataExchange.builder()
                    .putAll(block.getBlock(), "block", "block_broken")
                    .putAll(block.getCruxBlock(), "crux_block")
                    .putAll(p, "miner", "entity")
                    .build()).build();

            Location spawn = block.getBlock().getLocation().toCenterLocation();
            lootTable.populateLoot(ctx).forEach(drop ->{
                spawn.getWorld().dropItemNaturally(spawn, drop);
            });
        }

        if(itemDamage > 0 && p.getGameMode() != GameMode.CREATIVE){
            ItemStack check = event.getItem();
            if(!CruxItem.isEmpty(check)){
                p.damageItemStack(check, itemDamage);
            }
        }

        if(sound != null){
            sound.playAt(block.getBlock().getLocation().toCenterLocation());
        }

        return Event.Result.ALLOW;
    }
}
