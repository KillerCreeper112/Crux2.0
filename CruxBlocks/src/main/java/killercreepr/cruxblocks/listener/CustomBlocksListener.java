package killercreepr.cruxblocks.listener;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.util.CruxBlockUtil;
import killercreepr.crux.util.CruxLoc;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxInteractable;
import killercreepr.cruxblocks.block.context.PlaceBlockContextImpl;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.data.entity.MinerHolder;
import killercreepr.cruxblocks.manager.CruxBlockManager;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistTags;
import killercreepr.cruxblocks.user.BlockMiner;
import killercreepr.cruxblocks.user.EntityMiner;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

//todo the block breaking speed is wack
public class CustomBlocksListener implements Listener {
    protected final @NotNull CruxBlockManager manager;
    public CustomBlocksListener(@NotNull CruxBlockManager manager) {
        this.manager = manager;
    }

    public @NotNull CruxBlockManager getManager() {
        return manager;
    }

    private static final Collection<Material> REPLACE = Set.of(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR,
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

    @EventHandler
    private void blockDrop(BlockDropItemEvent event){
        Player p = event.getPlayer();
        Block block = event.getBlock();
        ActiveCruxBlock active = manager.getActiveBlock(block, event.getBlockState().getBlockData());
        if(active == null) return;

        event.getItems().clear();
        if(p.getGameMode() == GameMode.CREATIVE) return;

        CruxBlock crux = active.getCruxBlock();
        Collection<ItemStack> drops = active.getDrops(EntityMiner.from(p));
        if(crux.getSoundGroup() != null){
            block.getWorld().playSound(block.getLocation().toCenterLocation(), crux.getSoundGroup().getBreakSound(),
                crux.getSoundGroup().getVolume(), crux.getSoundGroup().getPitch());
        }
        if(drops == null) return;
        Location l = block.getLocation().toCenterLocation().subtract(0, .4, 0);
        for(ItemStack i : drops){
            l.getWorld().dropItemNaturally(l, i);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        Player p = event.getPlayer();
        if(event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;

        MinerHolder data = EntityMemory.getOrCreateDataHolder(p, MinerHolder.class);
        if(data==null) return;
        data.onMine(p);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        Player p = event.getPlayer();
        MinerHolder data = EntityMemory.getOrCreateDataHolder(p, MinerHolder.class);
        if(data==null) return;
        data.onMine(p, event.getBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        ActiveCruxBlock active = manager.getActiveBlock(b);
        if(active==null) return;
        Player p = event.getPlayer();
        active.breakBlock(EntityMiner.from(event.getPlayer()), false, p.getGameMode() == GameMode.CREATIVE);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreakBlock(BlockBreakBlockEvent event) {
        Block b = event.getBlock();
        ActiveCruxBlock active = manager.getActiveBlock(b);
        if(active==null) return;
        active.breakBlock(new BlockMiner(event.getSource()), false, false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        Player p = event.getPlayer();
        MinerHolder data = EntityMemory.getOrCreateDataHolder(p, MinerHolder.class);
        if(data==null) return;
        data.setLastMine(null);
        data.resetBreakSpeed(p);
    }

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

        Block clickedBlock = event.getClickedBlock();
        if(event.getAction().isLeftClick() && clickedBlock != null){
            if(p.getGameMode() == GameMode.CREATIVE) return;
            MinerHolder data = EntityMemory.getOrCreateDataHolder(p, MinerHolder.class);
            if(data==null) return;
            data.onMine(p, clickedBlock);
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

        ActiveCruxBlock customClicked = manager.getActiveBlock(clickedBlock);
        if(customClicked instanceof ActiveCruxInteractable i){
            if((p.isSneaking() && item == null) || !(p.isSneaking())){
                //event.setCancelled(true);
                if(i.interact(event) == Event.Result.ALLOW){
                    p.swingMainHand();
                    return;
                }
            }
        }
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
                ActiveCruxBlock placed = group.placeBlock(new PlaceBlockContextImpl(placeBlock, EntityMiner.from(p), blockFace));
                if(placed == null) return;

                if(p.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);
                p.swingHand(event.getHand());
            }
        }.runTaskLater(Crux.getMainPlugin(), 1L);
    }
}
