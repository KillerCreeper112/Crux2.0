package killercreepr.cruxblocks.core.listener;

import com.destroystokyo.paper.MaterialTags;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.CruxComponents;
import killercreepr.crux.core.util.CruxBlockUtil;
import killercreepr.crux.core.util.CruxItem;
import killercreepr.crux.core.util.CruxLoc;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxInteractable;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.flag.BlockBreakFlag;
import killercreepr.cruxblocks.api.block.flag.BlockBreakFlags;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.api.block.manager.CruxBlockManager;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.core.entity.memory.MinerHolder;
import killercreepr.cruxblocks.core.mining.user.BlockMiner;
import killercreepr.cruxblocks.core.persistence.CruxBlocksPersistTags;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

//todo the block breaking speed is a little wack
public class CustomBlocksListener implements Listener {
    protected final @NotNull Plugin plugin;
    protected final @NotNull CruxBlockManager manager;
    public CustomBlocksListener(@NotNull Plugin plugin, @NotNull CruxBlockManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public @NotNull CruxBlockManager getManager() {
        return manager;
    }

    private static final Collection<Material> REPLACE = Set.of(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR,
            Material.SHORT_GRASS, Material.SEAGRASS, Material.WATER, Material.LAVA, Material.TALL_GRASS);
    //private final static Map<UUID, Long> interactCooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        //interactCooldowns.remove(event.getPlayer().getUniqueId());
    }

    public float getExplosionPower(BlockState b, float fallBack){
        Material m = b.getType();
        if(m == Material.RESPAWN_ANCHOR || MaterialTags.BEDS.isTagged(m)) return 5f;
        if(m == Material.TNT) return 4f;
        return fallBack;
    }

    public float getExplosionPower(Entity e, float fallBack){
        if(e instanceof Creeper c) return c.getExplosionRadius();
        if(e instanceof Explosive c) return c.getYield();
        if(e instanceof Wither) return 7f;
        if(e instanceof EnderCrystal) return 6f;
        return fallBack;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockExplode(BlockExplodeEvent event) {
        float power = getExplosionPower(event.getExplodedBlockState(), 4f);
        event.blockList().removeIf(block ->{
            ActiveCruxBlock crux = manager.getActiveBlock(block);
            if(crux == null) return false;
            if(!crux.getCruxBlock().getComponents().has(CruxBlockComponents.EXPLOSION_RESISTANCE)) return false;
            float x = power - crux.getCruxBlock().getComponents().get(CruxBlockComponents.EXPLOSION_RESISTANCE);
            return x <= 0f;
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        float power = getExplosionPower(event.getEntity(), 4f);
        event.blockList().removeIf(block ->{
            ActiveCruxBlock crux = manager.getActiveBlock(block);
            if(crux == null) return false;
            if(!crux.getCruxBlock().getComponents().has(CruxBlockComponents.EXPLOSION_RESISTANCE)) return false;
            float x = power - crux.getCruxBlock().getComponents().get(CruxBlockComponents.EXPLOSION_RESISTANCE);
            return x <= 0f;
        });
    }


    @EventHandler
    private void onNotePlay(NotePlayEvent event){
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block b = event.getBlock();
        ActiveCruxBlock block = manager.getActiveBlock(b);
        if(block == null) return;
        block.update();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for(Block b : event.getBlocks()){
            ActiveCruxBlock crux = manager.getActiveBlock(b);
            if(crux==null) continue;
            if(crux.getCruxBlock().getComponents().getOrDefault(CruxBlockComponents.PISTON_IMMOVABLE, false)){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        for(Block b : event.getBlocks()){
            ActiveCruxBlock crux = manager.getActiveBlock(b);
            if(crux==null) continue;
            if(crux.getCruxBlock().getComponents().getOrDefault(CruxBlockComponents.PISTON_IMMOVABLE, false)){
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtendTripWireBreak(BlockPistonExtendEvent event) {
        for(Block b : event.getBlocks()){
            if(b.getType() != Material.TRIPWIRE) continue;
            ActiveCruxBlock crux = manager.getActiveBlock(b);
            if(crux==null) continue;
            crux.breakBlock(Miner.block(event.getBlock()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonRetractTripWireBreak(BlockPistonRetractEvent event) {
        for(Block b : event.getBlocks()){
            if(b.getType() != Material.TRIPWIRE) continue;
            ActiveCruxBlock crux = manager.getActiveBlock(b);
            if(crux==null) continue;
            crux.breakBlock(Miner.block(event.getBlock()));
        }
    }


    @EventHandler
    private void blockDropItem(BlockDropItemEvent event){
        Player p = event.getPlayer();
        Block block = event.getBlock();
        ActiveCruxBlock active = manager.getActiveBlock(block, event.getBlockState().getBlockData());
        if(active == null) return;

        event.getItems().clear();
        if(p.getGameMode() == GameMode.CREATIVE) return;

        CruxBlock crux = active.getCruxBlock();
        Collection<ItemStack> drops = active.getDrops(Miner.entity(p.getInventory().getItemInMainHand(), p));
        CreateBlockSoundGroup soundGroup = crux.getComponents().getOrDefault(
            CruxBlockComponents.BLOCK_SOUND_GROUP, crux.getGroup().getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP)
        );
        if(soundGroup != null){
            CreateSound sound = soundGroup.getBreakSound();
            if(sound != null){
                sound.playAt(block.getLocation().toCenterLocation());
            }
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
        if(p.getGameMode() != GameMode.CREATIVE){
            if(active.getCruxBlock().getComponents().getOrDefault(CruxComponents.UNBREAKABLE, false)){
                event.setCancelled(true);
                return;
            }
        }

        BlockBreakFlags flags = BlockBreakFlags.flags();
        if(p.getGameMode() == GameMode.CREATIVE) flags.add(BlockBreakFlag.DISABLE_DROPS);
        active.breakBlock(Miner.entity(p.getInventory().getItemInMainHand(), p), flags);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreakBlock(BlockBreakBlockEvent event) {
        Block b = event.getBlock();
        ActiveCruxBlock active = manager.getActiveBlock(b);
        if(active==null) return;
        active.breakBlock(new BlockMiner(event.getSource()));
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
                BlockState replaced = placeBlock.getState(true);
                placeBlock.setType(item.getType());
                BlockPlaceEvent place = new BlockPlaceEvent(placeBlock, replaced, clickedBlock, item, p, true, event.getHand());
                if(!place.callEvent()){
                    event.setCancelled(true);
                    placeBlock.setType(replaced.getType());
                    placeBlock.setBlockData(replaced.getBlockData());
                    return;
                }
                Crux.handlers().block().setType(placeBlock, item.getType());
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
        if(clickedBlock.getType().asBlockType().isInteractable() && clickedBlock.getType() != Material.NOTE_BLOCK){
            if(!p.isSneaking() || (p.isSneaking() && item == null)) return;
        }
        if(CruxItem.isEmpty(item)) return;
        CruxItem cruxItem = CruxItem.create(item);

        CruxBlockGroup group = cruxItem.get(CruxBlockComponents.BLOCK_GROUP); //CruxBlocksPersistTags.CRUX_BLOCK_GROUP.get(item);
        if(group == null) return;
        Block placeBlock = getPlaceBlock(clickedBlock, blockFace);
        if(!placeBlock.isEmpty() && !placeBlock.isReplaceable()) return;
        //Check for entities in block.
        for(Entity e : clickedBlock.getWorld().getNearbyEntities(CruxBlockUtil.getBlockBox(placeBlock))){
            if(e instanceof LivingEntity) return;
        }
        EquipmentSlot hand = event.getHand();
        plugin.getServer().getScheduler().runTask(plugin, task ->{
            ActiveCruxBlock placed = group.placeBlock(PlaceBlockContext.context(placeBlock, Miner.entity(item, p, hand), blockFace));
            if(placed == null) return;

            if(p.getGameMode() != GameMode.CREATIVE) item.setAmount(item.getAmount() - 1);
            p.swingHand(hand);
        });
    }
}
