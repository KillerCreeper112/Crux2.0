package killercreepr.cruxblocks.core.listener;

import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.core.util.CruxEntityUtil;
import killercreepr.crux.core.util.CruxWorldUtil;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.registry.CruxBlockRegistry;
import killercreepr.cruxblocks.core.block.component.CruxBlockComponents;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.world.GenericGameEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class NoteBlockSoundsListener implements Listener {
    protected final Plugin plugin;
    protected final CruxBlockRegistry registry;

    public NoteBlockSoundsListener(Plugin plugin, CruxBlockRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGenericGame(final GenericGameEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        if (!CruxWorldUtil.isLoaded(entity.getLocation())) return;

        GameEvent gameEvent = event.getEvent();
        Block block = CruxEntityUtil.getBlockStandingOn(entity);
        EntityDamageEvent cause = entity.getLastDamageCause();

        if (gameEvent == GameEvent.HIT_GROUND && cause != null && cause.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (block == null || block.isEmpty()) return;

        CruxBlock crux = registry.getByBlock(block);
        if(crux==null) return;

        CreateBlockSoundGroup sounds = crux.getComponents().getOrDefault(
            CruxBlockComponents.BLOCK_SOUND_GROUP, crux.getGroup().getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP)
        );
        if(sounds==null) return;

        CreateSound sound;
        if (gameEvent == GameEvent.STEP) {
            sound = sounds.getStepSound();
        } else if (gameEvent == GameEvent.HIT_GROUND) {
            sound = sounds.getFallSound();
        } else return;
        if(sound==null) return;
        sound.playAt(entity.getLocation());
    }

    private final Map<Block, BreakerSound> breakerPlaySound = new HashMap<>();
    private final Map<UUID, BreakerSound> breakerPlaySoundByOwner = new HashMap<>();

    private BreakerSound getBreakerSound(Block loc){
        return breakerPlaySound.get(loc);
    }
    private BreakerSound getBreakerSound(UUID owner){
        return breakerPlaySoundByOwner.get(owner);
    }

    private void setBreakerSound(BreakerSound sound){
        BreakerSound old = breakerPlaySound.put(sound.location, sound);
        BreakerSound old1 = breakerPlaySoundByOwner.put(sound.uuid, sound);

        if(old != null && !old.isCancelled()) old.cancel();
        if(old1 != null && !old1.isCancelled()) old1.cancel();
    }

    private BreakerSound removeBreakerSound(Block loc){
        BreakerSound sound = breakerPlaySound.remove(loc);
        if(sound == null) return null;
        breakerPlaySoundByOwner.remove(sound.uuid);
        return sound.isCancelled() ? null : sound;
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        for (Map.Entry<Block, BreakerSound> entry : new HashSet<>(breakerPlaySound.entrySet())) {
            if (entry.getKey().getLocation().isWorldLoaded()) continue;
            if(entry.getValue().isCancelled()){
                removeBreakerSound(entry.getKey());
                continue;
            }
            entry.getValue().cancel();
            removeBreakerSound(entry.getKey());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placed = event.getBlockPlaced();
        CruxBlock crux = registry.getByBlock(placed);
        if(crux==null) return;
        CreateBlockSoundGroup sounds = crux.getComponents().getOrDefault(
            CruxBlockComponents.BLOCK_SOUND_GROUP, crux.getGroup().getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP)
        );
        if(sounds==null) return;
        CreateSound sound = sounds.getPlaceSound();
        if(sound==null) return;
        sound.playAt(placed.getLocation().toCenterLocation());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        Location location = block.getLocation().toCenterLocation();

        if (breakerPlaySound.containsKey(block)) {
            BreakerSound sound = removeBreakerSound(block);
            if(sound != null) sound.cancel();
        }

        CruxBlock crux = registry.getByBlock(block);
        if(crux==null) return;
        CreateBlockSoundGroup sounds = crux.getComponents().getOrDefault(
            CruxBlockComponents.BLOCK_SOUND_GROUP, crux.getGroup().getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP)
        );
        if(sounds==null) return;
        CreateSound sound = sounds.getBreakSound();
        if(sound==null) return;

        sound.playAt(location);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        BreakerSound sound = removeBreakerSound(event.getBlock());
        if(sound != null) sound.cancel();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getInstaBreak()) return;
        Block block = event.getBlock();
        if(block.getType() == Material.TRIPWIRE) return;
        CruxBlock crux = registry.getByBlock(block);
        if(crux==null) return;
        Location location = block.getLocation().toCenterLocation();

        CreateBlockSoundGroup soundGroup = crux.getComponents().getOrDefault(
            CruxBlockComponents.BLOCK_SOUND_GROUP, crux.getGroup().getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP)
        );
        if(soundGroup==null) return;
        CreateSound sound = soundGroup.getHitSound();
        if(sound==null) return;
        if (breakerPlaySound.containsKey(block)) return;

        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> sound.playAt(location), 2L, 4L);
        setBreakerSound(new BreakerSound(event.getPlayer().getUniqueId(), block, task));
        //breakerPlaySound.put(block, task);
    }


    private record BreakerSound(UUID uuid, Block location, BukkitTask task){
        public boolean isCancelled(){
            return task.isCancelled();
        }

        public void cancel(){
            task.cancel();
        }
    }
}
