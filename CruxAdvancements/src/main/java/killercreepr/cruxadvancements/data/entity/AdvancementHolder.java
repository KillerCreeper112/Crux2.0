package killercreepr.cruxadvancements.data.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import killercreepr.crux.Crux;
import killercreepr.crux.data.Loadable;
import killercreepr.crux.data.entity.PlayerDataHolder;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.data.AdvancementTracker;
import killercreepr.cruxadvancements.data.TrackedAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.registries.AdvancementRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

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

    protected final AdvancementTracker advancementTracker = new AdvancementTracker();

    public AdvancementTracker getAdvancementTracker() {
        return advancementTracker;
    }

    public @NotNull CruxJson getSaveFile(){
        return new CruxJson(plugin, "data/cruxadvancements/player/" + parent.getUUID());
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
        cfg.close();
        advancementTracker.setTrackedAdvancements(tracked);
        loadGlobal();
    }
}
