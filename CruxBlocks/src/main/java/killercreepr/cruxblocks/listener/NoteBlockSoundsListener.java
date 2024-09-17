package killercreepr.cruxblocks.listener;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.data.communication.CreateSound;
import killercreepr.crux.util.CruxEntity;
import killercreepr.crux.util.CruxWorldUtil;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.registries.CruxBlockRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.Location;
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
import java.util.Map;

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
        Block block = CruxEntity.getBlockStandingOn(entity);
        EntityDamageEvent cause = entity.getLastDamageCause();

        if (gameEvent == GameEvent.HIT_GROUND && cause != null && cause.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (block == null || block.isEmpty()) return;

        CruxBlock crux = registry.getByBlock(block);
        if(crux==null) return;

        CreateBlockSoundGroup sounds = crux.getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP);
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

    private final Map<Location, BukkitTask> breakerPlaySound = new HashMap<>();

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        for (Map.Entry<Location, BukkitTask> entry : breakerPlaySound.entrySet()) {
            if (entry.getKey().isWorldLoaded() || entry.getValue().isCancelled()) continue;
            entry.getValue().cancel();
            breakerPlaySound.remove(entry.getKey());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placed = event.getBlockPlaced();
        CruxBlock crux = registry.getByBlock(placed);
        if(crux==null) return;
        CreateBlockSoundGroup sounds = crux.getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP);
        if(sounds==null) return;
        CreateSound sound = sounds.getPlaceSound();
        if(sound==null) return;
        sound.playAt(placed.getLocation().toCenterLocation());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        final Block block = event.getBlock();
        Location location = block.getLocation().toCenterLocation();

        if (breakerPlaySound.containsKey(location)) {
            breakerPlaySound.get(location).cancel();
            breakerPlaySound.remove(location);
        }

        CruxBlock crux = registry.getByBlock(block);
        if(crux==null) return;
        CreateBlockSoundGroup sounds = crux.getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP);
        if(sounds==null) return;
        CreateSound sound = sounds.getBreakSound();
        if(sound==null) return;

        sound.playAt(location);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        Location location = event.getBlock().getLocation().toCenterLocation();
        if (breakerPlaySound.containsKey(location)) {
            breakerPlaySound.get(location).cancel();
            breakerPlaySound.remove(location);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getInstaBreak()) return;
        Block block = event.getBlock();
        CruxBlock crux = registry.getByBlock(block);
        if(crux==null) return;
        Location location = block.getLocation().toCenterLocation();

        CreateBlockSoundGroup soundGroup = crux.getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP);
        if(soundGroup==null) return;
        CreateSound sound = soundGroup.getHitSound();
        if(sound==null) return;
        if (breakerPlaySound.containsKey(location)) return;

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> sound.playAt(location), 2L, 4L);
        breakerPlaySound.put(location, task);
    }

}
