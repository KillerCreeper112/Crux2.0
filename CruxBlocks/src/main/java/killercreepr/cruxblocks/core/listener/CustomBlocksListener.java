package killercreepr.cruxblocks.core.listener;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.core.component.CruxComponents;
import killercreepr.crux.core.util.CruxBlockFace;
import killercreepr.crux.core.util.CruxBlockUtil;
import killercreepr.crux.core.util.CruxEntityUtil;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.*;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.flag.BlockBreakFlag;
import killercreepr.cruxblocks.api.block.flag.BlockBreakFlags;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.api.block.manager.CruxBlockManager;
import killercreepr.cruxblocks.api.event.CustomBlockExplodeEvent;
import killercreepr.cruxblocks.api.event.CustomEntityExplodeEvent;
import killercreepr.cruxblocks.api.event.CustomExplodeEvent;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.core.entity.memory.MinerHolder;
import killercreepr.cruxblocks.core.mining.user.BlockMiner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    //private final static Map<UUID, Long> interactCooldowns = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block b = event.getBlockPlaced();
        if(b.getType() == Material.TRIPWIRE){
            event.setCancelled(true);
            Communicator.chat("<red>Tripwire has been disabled.")
                .use(event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        Block block = CruxEntityUtil.getBlockStandingOn(e);
        if(block == null) return;
        var active = manager.getActiveBlock(block);
        if(active instanceof ActiveCruxEntityDamageOn dmg){
            dmg.onEntityDamage(event);
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        BlockData data = block.getBlockData();
        if(data instanceof RedstoneWire wire){
            for(BlockFace face : wire.getAllowedFaces()){
                if(face.isCartesian() && face != BlockFace.UP && face != BlockFace.DOWN){
                    Block check = block.getRelative(face).getRelative(BlockFace.DOWN);
                    ActiveCruxBlock active = manager.getActiveBlock(check);
                    if(active instanceof ActiveCruxRedstonePowerable powerable && powerable.isRedstonePowerable()){
                        powerable.redstonePowerChanged(block,event.getNewCurrent());
                    }
                }

                var connection = wire.getFace(face);
                if(connection == RedstoneWire.Connection.NONE) continue;
                BlockFace dir = connection == RedstoneWire.Connection.UP ? BlockFace.UP :  face;
                Block check = block.getRelative(dir);
                ActiveCruxBlock active = manager.getActiveBlock(check);
                if(!(active instanceof ActiveCruxRedstonePowerable powerable)) continue;
                if(!powerable.isRedstonePowerable()) continue;
                powerable.redstonePowerChanged(block,event.getNewCurrent());
            }
            return;
        }
        if(data instanceof AnaloguePowerable){
            for(BlockFace face : CruxBlockFace.CARTESIAN){
                if(face.isCartesian() && face != BlockFace.UP && face != BlockFace.DOWN){
                    Block check = block.getRelative(face).getRelative(BlockFace.DOWN);
                    ActiveCruxBlock active = manager.getActiveBlock(check);
                    if(active instanceof ActiveCruxRedstonePowerable powerable && powerable.isRedstonePowerable()){
                        powerable.redstonePowerChanged(block,event.getNewCurrent());
                    }
                }

                Block check = block.getRelative(face);
                ActiveCruxBlock active = manager.getActiveBlock(check);
                if(!(active instanceof ActiveCruxRedstonePowerable powerable)) continue;
                if(!powerable.isRedstonePowerable()) continue;
                powerable.redstonePowerChanged(block,event.getNewCurrent());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        nmsSkip.remove(p.getUniqueId());
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockExplode(BlockExplodeEvent event) {
        float power = getExplosionPower(event.getExplodedBlockState(), 4f);
        List<Block> list = new ArrayList<>();
        event.blockList().removeIf(b ->{
            ActiveCruxBlock crux = manager.getActiveBlock(b);
            if(crux == null){
                list.add(b);
                return false;
            }

            if(!crux.getCruxBlock().getComponents().has(CruxBlockComponents.EXPLOSION_RESISTANCE)){
                list.add(b);
                return true;
            }
            float x = power - crux.getCruxBlock().getComponents().get(CruxBlockComponents.EXPLOSION_RESISTANCE);
            if(x <= 0f){
                return true;
            }
            list.add(b);
            return true;
        });
        CustomBlockExplodeEvent customEvent = new CustomBlockExplodeEvent(
            event.getBlock().getLocation(), list, event.blockList(), event.getExplosionResult(), event.getYield(), event.getBlock()
        );
        if(!customEvent.callEvent()){
            event.setCancelled(true);
        }
        handleCustomExplosion(customEvent, Miner.block(event.getBlock()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(!event.hasChangedPosition()) return;
        var p = event.getPlayer();
        Block ground = CruxEntityUtil.getBlockStandingOn(p);
        if(ground == null) return;
        ActiveCruxBlock active = manager.getActiveBlock(ground);
        if(active instanceof ActiveCruxEntityMove move) move.onEntityMove(p);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityMove(EntityMoveEvent event) {
        if(!event.hasChangedPosition()) return;
        var p = event.getEntity();
        Block ground = CruxEntityUtil.getBlockStandingOn(p);
        if(ground == null) return;
        ActiveCruxBlock active = manager.getActiveBlock(ground);
        if(active instanceof ActiveCruxEntityMove move) move.onEntityMove(p);
    }


    public void handleCustomExplosion(CustomExplodeEvent event, Miner miner){
        switch (event.getResult()){
            case DESTROY, DESTROY_WITH_DECAY -> {}
            default -> { return; }
        }
        float yield = event.getYield() * 100f;
        event.getBlocks().forEach(b ->{
            ActiveCruxBlock crux = manager.getActiveBlock(b);
            if(crux == null) return;
            BlockBreakFlags flags;
            if(CruxMath.testChance(yield)){
                flags = BlockBreakFlags.empty();
            }else flags = BlockBreakFlags.flags(BlockBreakFlag.DISABLE_DROPS);
            crux.breakBlock(miner, flags);
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        float power = getExplosionPower(event.getEntity(), 4f);
        List<Block> list = new ArrayList<>();
        event.blockList().removeIf(b ->{
            ActiveCruxBlock crux = manager.getActiveBlock(b);
            if(crux == null){
                list.add(b);
                return false;
            }

            if(!crux.getCruxBlock().getComponents().has(CruxBlockComponents.EXPLOSION_RESISTANCE)){
                list.add(b);
                return true;
            }
            float x = power - crux.getCruxBlock().getComponents().get(CruxBlockComponents.EXPLOSION_RESISTANCE);
            if(x <= 0f){
                return true;
            }
            list.add(b);
            return true;
        });
        CustomEntityExplodeEvent customEvent = new CustomEntityExplodeEvent(
            event.getEntity().getLocation(), list, event.blockList(), event.getExplosionResult(), event.getYield(), event.getEntity()
        );
        if(!customEvent.callEvent()){
            event.setCancelled(true);
        }

        handleCustomExplosion(customEvent, Miner.entity(event.getEntity()));
        /*float power = getExplosionPower(event.getEntity(), 4f);
        event.blockList().removeIf(block ->{
            ActiveCruxBlock crux = manager.getActiveBlock(block);
            if(crux == null) return false;
            if(!crux.getCruxBlock().getComponents().has(CruxBlockComponents.EXPLOSION_RESISTANCE)) return false;
            float x = power - crux.getCruxBlock().getComponents().get(CruxBlockComponents.EXPLOSION_RESISTANCE);
            return x <= 0f;
        });*/
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
    public void onBlockDestroy(BlockDestroyEvent event) {
        Block b = event.getBlock();
        ActiveCruxBlock active = manager.getActiveBlock(b);
        if(active==null) return;
        event.setWillDrop(false);
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
    public void onBlockFromTo(BlockFromToEvent event) {
        Block to = event.getToBlock();
        ActiveCruxBlock active = manager.getActiveBlock(to);
        if(active==null) return;
        event.setCancelled(true);
        active.breakBlock(Miner.block(event.getBlock()));
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
        if(clicked.isReplaceable()) return clicked;
        /*if (REPLACE.contains(clicked.getType()) ||
                (clicked.getType().equals(Material.SNOW) && ((Snow) clicked.getBlockData()).getLayers() == 1)) return clicked;*/
        return clicked.getRelative(blockFace);
    }

    public boolean isVanillaUsableItem(ItemStack item){
        Material type = item.getType();
        ItemType itemType = type.asItemType();
        if(itemType.hasBlockType()) return true;
        if(MaterialSetTag.ITEMS_BOATS.isTagged(type)) return true;
        if(MaterialTags.SPAWN_EGGS.isTagged(type)) return true;
        if(MaterialTags.BUCKETS.isTagged(type)) return true;
        if(type == Material.ARMOR_STAND) return true;
        if(type == Material.PAINTING) return true;
        if(type == Material.ITEM_FRAME) return true;
        if(type == Material.GLOW_ITEM_FRAME) return true;
        return false;
    }

    public boolean checkForPass(ItemStack item){
        Material type = item.getType();
        if(MaterialSetTag.ITEMS_BOATS.isTagged(type)) return true;
        if(type == Material.PAINTING) return true;
        if(type == Material.ITEM_FRAME) return true;
        if(type == Material.GLOW_ITEM_FRAME) return true;
        if(type == Material.BUCKET) return true;
        return false;
    }

    public void nmsPlaceBucket(Block placeBlock, net.minecraft.world.entity.player.Player nmsPlayer,
                               net.minecraft.world.item.ItemStack nmsItem,
                               InteractionHand hand,
                               Direction direction, BlockHitResult result, BlockPos nmsPos, BucketItem bucketItem){
        if(!bucketItem.emptyContents(
            nmsPlayer, nmsPlayer.getCommandSenderWorld(), new BlockPos(placeBlock.getX(), placeBlock.getY(), placeBlock.getZ()),
            result, direction, nmsPos,
            nmsItem, hand
        )) return;
        bucketItem.checkExtraContent(nmsPlayer, nmsPlayer.getCommandSenderWorld(), nmsItem, nmsPos);
        nmsPlayer.awardStat(Stats.ITEM_USED.get(bucketItem));
        net.minecraft.world.item.ItemStack itemStack = ItemUtils.createFilledResult(nmsItem, nmsPlayer, BucketItem.getEmptySuccessItem(nmsItem, nmsPlayer));
        nmsPlayer.setItemInHand(hand, itemStack);
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level level, net.minecraft.world.entity.player.Player player, ClipContext.Fluid fluidMode) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 vec3 = eyePosition.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(player.blockInteractionRange()));
        return level.clip(new ClipContext(eyePosition, vec3, net.minecraft.world.level.ClipContext.Block.OUTLINE, fluidMode, player));
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        Block b = event.getBlock();
        if(b != null){
            Material type = b.getType();
            if(type == Material.TRIPWIRE){
                event.setCancelled(true);
                ActiveCruxBlock active = manager.getActiveBlock(b);
                if(active instanceof ActiveCruxEntityPhysicalInteract i){
                    i.onEntityPhysicalInteract(event.getEntity(), event);
                }
            }
        }
    }


    protected final Map<UUID, Boolean> nmsSkip = new HashMap<>();
    @EventHandler
    private void interact(PlayerInteractEvent event){
        if(event.getAction() == Action.PHYSICAL){
            if(event.getClickedBlock() != null){
                Material type = event.getClickedBlock().getType();
                if(type == Material.TRIPWIRE){
                    event.setCancelled(true);
                    ActiveCruxBlock active = manager.getActiveBlock(event.getClickedBlock());
                    if(active instanceof ActiveCruxEntityPhysicalInteract i){
                        i.onEntityPhysicalInteract(event.getPlayer(), event);
                    }
                }
            }
            return;
        }
        Player p = event.getPlayer();
        if(nmsSkip.containsKey(p.getUniqueId())){
            nmsSkip.remove(p.getUniqueId());
            return;
        }

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
                Event.Result result = i.interact(event);
                if(result == Event.Result.ALLOW){
                    p.swingMainHand();
                    return;
                }
            }
        }
        if((clickedBlock.getType() == Material.NOTE_BLOCK)){
            if(item != null && isVanillaUsableItem(item) && event.useInteractedBlock() == Event.Result.DENY){
                Block placeBlock = getPlaceBlock(clickedBlock, blockFace);
                net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer) p).getHandle();
                net.minecraft.world.item.ItemStack nmsItem = ((CraftItemStack)item.ensureServerConversions()).handle;
                InteractionHand hand = event.getHand() == EquipmentSlot.HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                Location point = event.getInteractionPoint();
                Direction nmsDirection = Direction.valueOf(event.getBlockFace().toString());
                BlockPos nmsPos = new BlockPos(placeBlock.getX(), placeBlock.getY(), placeBlock.getZ());
                BlockHitResult result = new BlockHitResult(
                    new Vec3(point.getX(), point.getY(), point.getZ()),
                    nmsDirection,
                    nmsPos, false
                );

                if(nmsItem.getItem() instanceof BucketItem bucketItem && item.getType() != Material.BUCKET){
                    nmsPlaceBucket(
                        placeBlock, nmsPlayer, nmsItem, hand,
                        nmsDirection, result, nmsPos, bucketItem
                    );
                    return;
                }
                Material itemType = item.getType();


                UseOnContext ctx = new UseOnContext(nmsPlayer, hand, result);
                InteractionResult nmsResult = nmsItem.useOn(ctx);
                if(nmsResult == InteractionResult.PASS && checkForPass(item)){
                    nmsSkip.put(p.getUniqueId(), true);
                    InteractionResult result2 = nmsItem.use(nmsPlayer.getCommandSenderWorld(), nmsPlayer, hand);
                    if(result2 instanceof InteractionResult.Success success && success.heldItemTransformedTo() != null){
                        if(MaterialSetTag.ITEMS_BOATS.isTagged(itemType)){
                            ItemStack gotted = p.getInventory().getItemInMainHand();
                            if(!CruxItem.isEmpty(gotted)){
                                gotted.setAmount(gotted.getAmount()-1);
                            }
                        }else nmsPlayer.setItemInHand(hand, success.heldItemTransformedTo());
                    }
                    nmsSkip.remove(p.getUniqueId());
                }
                /*Block placeBlock = getPlaceBlock(clickedBlock, blockFace);
                if(item.getType().isSolid()){
                    for(Entity e : clickedBlock.getWorld().getNearbyEntities(CruxBlockUtil.getBlockBox(placeBlock).expand(.05D))){
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
                        placeBlock.getBlockSoundGroup().getPitch());*/
                return;
            }
        }
        if(clickedBlock.getType().asBlockType().isInteractable() && clickedBlock.getType() != Material.NOTE_BLOCK){
            if(!p.isSneaking() || (p.isSneaking() && item == null)) return;
        }
        if(CruxItem.isEmpty(item)) return;
        CruxItem cruxItem = CruxItem.wrap(item);

        CruxBlockGroup group = cruxItem.getOrDefaultData(CruxBlockComponents.BLOCK_GROUP);
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
