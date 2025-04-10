package killercreepr.cruxadvancements.core.entity.memory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import killercreepr.crux.api.data.Loadable;
import killercreepr.crux.api.entity.memory.PlayerMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.PlayerDataHolder;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.values.ValuesProvider;
import killercreepr.cruxadvancements.core.CruxAdvancementsModule;
import killercreepr.cruxadvancements.core.data.AdvancementTracker;
import killercreepr.cruxadvancements.core.data.TrackedAdvancement;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import killercreepr.cruxadvancements.core.stat.AdvancementStats;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import killercreepr.cruxstats.api.bukkit.BukkitStatHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AdvancementHolder extends PlayerDataHolder implements Loadable {
    public static final Key KEY = Crux.key("advancement");
    protected final @NotNull Plugin plugin;
    public AdvancementHolder(@NotNull PlayerMemory parent, @NotNull Plugin plugin) {
        this(KEY, parent, plugin);
    }
    public AdvancementHolder(@NotNull Key key, @NotNull PlayerMemory parent, @NotNull Plugin plugin) {
        super(key, parent);
        this.plugin = plugin;
        load();
    }

    protected final Map<Key, Long> timeAdvancementCompleted = new HashMap<>();
    public void onAdvancementComplete(CruxAdvancementManager<?> manager, CruxAdvancement advancement){
        TrackedAdvancement tracked = advancementTracker.getNonGlobalTrackedAdvancement(manager.key(), advancement.key());
        advancementTracker.untrack(manager.key(), advancement.key());
        if(tracked == null) return;
        timeAdvancementCompleted.put(tracked.getAdvancementKey(), tracked.getTimeStarted());
    }

    protected final AdvancementTracker advancementTracker = new AdvancementTracker();

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    public int getMaxTrackedAdvancements() {
        return (int) BukkitStatHolder.holder(parent.getUUID()).getStatValue(AdvancementStats.MAX_TRACKABLE_ADVANCEMENTS);
    }

    public AdvancementTracker getAdvancementTracker() {
        return advancementTracker;
    }

    public @NotNull CruxJson getSaveFile(){
        return new CruxJson(plugin, "data/cruxadvancements/player/" + parent.getUUID());
    }

    public static @NotNull CruxJson getDefaultSaveFile(UUID uuid){
        return new CruxJson(Crux.getMainPlugin(), "data/cruxadvancements/player/" + uuid);
    }

    @Override
    protected void removingFromMemory(@Nullable Entity e) {
        super.removingFromMemory(e);
        save();
    }

    @Override
    public void save() {
        CruxJson cfg = getSaveFile();
        cfg.reloadIfNeeded();
        JsonObject json = cfg.json();
        if(json==null) return;
        JsonRegistry registry = cfg.jsonRegistry();
        JsonArray a = new JsonArray();
        advancementTracker.getTrackedAdvancements().forEach(tracked ->{
            if(tracked.isGlobal()) return;
            a.add(registry.serializeToJson(tracked));
        });
        json.add("tracked_advancements", a);

        JsonObject timeStarted;
        if(json.get("time_started") instanceof JsonObject e) timeStarted = e;
        else timeStarted = new JsonObject();
        timeAdvancementCompleted.forEach((key, time) -> timeStarted.addProperty(key.asString(), time));
        json.add("time_started", timeStarted);
        cfg.save();
    }

    public void loadGlobal(){
        AdvancementRegistries.GLOBAL_ADVANCEMENTS.forEach(tracked ->{
            if(advancementTracker.isTracking(tracked)) return;
            CruxAdvancement advancement = tracked.getAdvancement();
            if(advancement == null) return;
            if(advancement.isGranted(parent.getUUID())) return;
            advancementTracker.track(tracked);
        });
    }

    public int getDefaultMaxTrackedAdvancements(){
        ValuesProvider cfg = CruxRegistries.MODULES.getModuleOrThrow(CruxAdvancementsModule.class).values();
        return cfg.DEFAULT_MAX_TRACKED_ADVANCEMENTS().value().intValue();
    }

    @Override
    public void load() {
        CruxJson cfg = getSaveFile();
        JsonObject json = cfg.json();
        if(json==null){
            loadGlobal();
            return;
        }
        JsonRegistry registry = cfg.jsonRegistry();
        Collection<TrackedAdvancement> tracked = new HashSet<>();
        if(json.get("tracked_advancements") instanceof JsonArray a){
            a.forEach(ele ->{
                TrackedAdvancement t = registry.deserializeFromJson(TrackedAdvancement.class, ele);
                if(t==null) return;
                tracked.add(t);
            });
        }
        Number maxTracked = cfg.get("max_tracked_advancements", Number.class);
        cfg.close();
        advancementTracker.setTrackedAdvancements(tracked);
        loadGlobal();
    }
}
