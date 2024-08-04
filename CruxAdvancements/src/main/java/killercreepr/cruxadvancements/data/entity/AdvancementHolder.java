package killercreepr.cruxadvancements.data.entity;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import killercreepr.crux.Crux;
import killercreepr.crux.data.Loadable;
import killercreepr.crux.data.entity.PlayerDataHolder;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.cruxadvancements.data.AdvancementTracker;
import killercreepr.cruxadvancements.data.TrackedAdvancement;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

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
        json.add("tracked_advancements", registry.serializeObject(advancementTracker.getTrackedAdvancements()));
        cfg.save();
    }

    @Override
    public void load() {
        CruxJson cfg = getSaveFile();
        JsonObject json = cfg.json();
        if(json==null) return;
        JsonRegistry registry = cfg.jsonRegistry();
        Collection<TrackedAdvancement> tracked = registry.deserialize(new TypeToken<Collection<TrackedAdvancement>>(){}.getType(),
            json.get("tracked_advancements"));
        cfg.close();
        if(tracked != null){
            advancementTracker.setTrackedAdvancements(tracked);
        }
    }
}
