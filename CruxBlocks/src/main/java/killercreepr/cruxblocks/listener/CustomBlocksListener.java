package killercreepr.cruxblocks.listener;

import killercreepr.crux.Crux;
import killercreepr.crux.util.CruxLoc;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.PlaceBlockContextImpl;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistTags;
import killercreepr.cruxblocks.util.CruxBlockUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomBlocksListener implements Listener {
    private static final List<Material> REPLACE = List.of(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR,
            Material.SHORT_GRASS, Material.SEAGRASS, Material.WATER, Material.LAVA, Material.TALL_GRASS);
    private final static Map<UUID, Long> interactCooldowns = new HashMap<>();


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        interactCooldowns.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onNotePlay(NotePlayEvent event){
        event.setCancelled(true);
    }

    /*todo @EventHandler
    private void blockDrop(BlockDropItemEvent event){
        Player p = event.getPlayer();
        ActiveBlock active = CustomBlock.getActiveBlock(event.getBlock(), event.getBlockState().getBlockData());
        if(active == null) return;
        event.getItems().clear();
        Collection<ItemStack> drops = active.getDrops(p, p.getInventory().getItemInMainHand());
        if(active.getCustom().getSoundGroup() != null){
            event.getBlock().getWorld().playSound(event.getBlock().getLocation(), active.getCustom().getSoundGroup().getBreakSound(),
                    active.getCustom().getSoundGroup().getVolume(), active.getCustom().getSoundGroup().getPitch());
        }
        if(drops == null || p.getGameMode() == GameMode.CREATIVE) return;
        Location l = event.getBlock().getLocation().add(.5, .1, .5);
        for(ItemStack i : drops){
            p.getWorld().dropItemNaturally(l, i);
        }
    }*/

    private Block getPlaceBlock(Block clicked, BlockFace blockFace){
        if (REPLACE.contains(clicked.getType()) ||
                (clicked.getType().equals(Material.SNOW) && ((Snow) clicked.getBlockData()).getLayers() == 1)) return clicked;
        return clicked.getRelative(blockFace);
    }

    @EventHandler
    private void interact(PlayerInteractEvent event){
        if(event.getAction() == Action.PHYSICAL){
            if(event.getClickedBlock() != null){
                Material type = event.getClickedBlock().getType();
                if(type == Material.TRIPWIRE) event.setCancelled(true);
            }
            return;
        }
        Player p = event.getPlayer();
        if(interactCooldowns.getOrDefault(p.getUniqueId(), 0L) > System.currentTimeMillis()){
            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock == null) return;
            if(clickedBlock.getType() == Material.NOTE_BLOCK || clickedBlock.getType() == Material.TRIPWIRE) event.setCancelled(true);
            return;
        }
        interactCooldowns.put(p.getUniqueId(), System.currentTimeMillis() + (50L));
        Block clickedBlock = event.getClickedBlock();
        if(event.getAction().isLeftClick() && clickedBlock != null){
            if(p.getGameMode() == GameMode.CREATIVE) return;
            /*ActiveBlock data = CustomBlock.getActiveBlock(clickedBlock);
            if(data != null) event.setCancelled(true);*/
            return;
        }
        ItemStack item = event.getItem();
        if(!event.getAction().isRightClick() || clickedBlock == null) return;
        if((clickedBlock.getType() == Material.NOTE_BLOCK)){
            if(!p.isSneaking() || (p.isSneaking() && item == null)) event.setCancelled(true);
        }
        BlockFace blockFace = event.getBlockFace();

        /*ActiveBlock customClicked = CustomBlock.getActiveBlock(clickedBlock);
        if(customClicked instanceof ActiveInteract i){
            if((p.isSneaking() && item == null) || !(p.isSneaking())){
                event.setCancelled(true);
                if(i.interact(event) == Event.Result.ALLOW){
                    p.swingMainHand();
                    return;
                }
            }
        }*/
        if((clickedBlock.getType() == Material.NOTE_BLOCK)){
            if(item != null && item.getType().isBlock() && event.useInteractedBlock() == Event.Result.DENY){
                Block placeBlock = getPlaceBlock(clickedBlock, blockFace);
                if(item.getType().isSolid()){
                    for(Entity e : clickedBlock.getWorld().getNearbyEntities(CruxBlockUtil.getBlockBox(placeBlock))){
                        if(e instanceof LivingEntity) return;
                    }
                }
                if(!placeBlock.canPlace(item.getType().createBlockData())) return;
                placeBlock.setType(item.getType());
                if(placeBlock.getBlockData() instanceof Directional dir){
                    float pitch = p.getLocation().getPitch();
                    BlockFace face = p.getFacing().getOppositeFace();
                    if(dir.getFaces().contains(BlockFace.UP)) if(pitch <= -45) face = BlockFace.DOWN;
                    if(dir.getFaces().contains(BlockFace.DOWN)) if(pitch >= 45) face = BlockFace.UP;
                    dir.setFacing(face);
                    placeBlock.setBlockData(dir);
                }else if(placeBlock.getBlockData() instanceof Orientable o) {
                    BlockFace face = blockFace.getOppositeFace();
                    o.setAxis(CruxLoc.convertBlockFaceToAxis(face));
                    placeBlock.setBlockData(o);
                }else if(placeBlock.getBlockData() instanceof Rotatable r) {
                    float pitch = p.getLocation().getPitch();
                    BlockFace face = p.getFacing().getOppositeFace();
                    if(pitch <= -45) face = BlockFace.DOWN;
                    else if(pitch >= 45) face = BlockFace.UP;
                    r.setRotation(face);
                    placeBlock.setBlockData(r);
                }
                if(p.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);

                p.getWorld().playSound(placeBlock.getLocation(),
                        placeBlock.getBlockSoundGroup().getPlaceSound(),
                        placeBlock.getBlockSoundGroup().getVolume(),
                        placeBlock.getBlockSoundGroup().getPitch());
                return;
            }
        }
        /*todo if(CruxBlocks.isInteractable(clickedBlock.getType()) && clickedBlock.getType() != Material.NOTE_BLOCK){
            if(!p.isSneaking() || (p.isSneaking() && item == null)) return;
        }*/
        CruxBlockGroup group = CruxBlocksPersistTags.CRUX_BLOCK_GROUP.get(item);
        if(group == null) return;
        Block placeBlock = getPlaceBlock(clickedBlock, blockFace);
        if(!placeBlock.isEmpty() && !placeBlock.isReplaceable()) return;
        //Check for entities in block.
        for(Entity e : clickedBlock.getWorld().getNearbyEntities(CruxBlockUtil.getBlockBox(placeBlock))){
            if(e instanceof LivingEntity) return;
        }
        new BukkitRunnable(){
            @Override
            public void run() {
                ActiveCruxBlock placed = group.placeBlock(new PlaceBlockContextImpl(placeBlock, p, blockFace));
                if(placed == null) return;

                if(p.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);
                p.swingMainHand();
            }
        }.runTaskLater(Crux.getMainPlugin(), 1L);
    }
}
