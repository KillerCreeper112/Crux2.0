package killercreepr.cruxadvancements.crazy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.crux.Crux;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.advancement.objective.progress.SimpleObjectiveProgression;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.config.CruxConfigHook;
import killercreepr.cruxadvancements.config.handler.FileCruxAdvancementProgress;
import killercreepr.cruxadvancements.config.handler.FileSimpleObjectiveProgression;
import killercreepr.cruxadvancements.config.loader.CrazyAdvancementCfgLoader;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.json.context.JsonContext;
import killercreepr.cruxconfig.config.common.json.registry.JsonRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

public class CfgCrazyAdvancementManager extends CrazyAdvancementManager<CrazyAdvancement> {
    public static CfgCrazyAdvancementManager createNew(@NotNull Key key, @NotNull Plugin plugin){
        return new CfgCrazyAdvancementManager(key, new AdvancementManager(CrazyUtil.toNameKey(key)), plugin);
    }

    protected final @NotNull Plugin plugin;
    public CfgCrazyAdvancementManager(@NotNull Key key, @NotNull AdvancementManager crazyManager, @NotNull Plugin plugin) {
        super(key, crazyManager);
        this.plugin = plugin;
    }

    public @NotNull CruxFolder getAdvancementsFolder(@NotNull Plugin plugin){
        return new CruxFolder(plugin, "advancements/" + key.value());
    }

    public @NotNull CruxJson getSaveFile(@NotNull Plugin plugin, @NotNull UUID player){
        return new CruxJson(plugin, "data/cruxadvancements/" + key.asString().replace(":", "_") +
            "/" + player);
    }

    public @NotNull Collection<CrazyAdvancement> parseAdvancements(@NotNull File folder){
        Collection<CrazyAdvancement> list = new HashSet<>();
        new CrazyAdvancementCfgLoader(list::add).loadConfiguration(folder);
        return list;
    }

    @Override
    public void save(@NotNull Plugin plugin) {

    }

    @Override
    public void refresh(@NotNull Plugin plugin) {
        for(Player p : plugin.getServer().getOnlinePlayers()){
            saveProgress(p.getUniqueId());
        }
        for(CrazyAdvancement a : new HashSet<>(advancements.values())){
            unregisterAdvancement(a);
        }
        load(plugin);

        for(Player p : plugin.getServer().getOnlinePlayers()){
            loadProgress(p.getUniqueId());
        }

        crazyManager.updateAdvancement(crazyAdvancements.values().toArray(new Advancement[0]));
    }

    @Override
    public void load(@NotNull Plugin plugin) {
        for(CrazyAdvancement a : parseAdvancements(getAdvancementsFolder(plugin).file())){
            registerAdvancement(a);
            Crux.log(Level.INFO, "Registered CrazyAdvancement: " + key() + " -> " + a.key());
            //a.load(getAdvancementSaveFile(plugin, a).file());
        }
        loadAllCrazyAdvancements();
    }

    @Override
    public void saveProgress(@NotNull UUID uuid, @NotNull CrazyAdvancement... advancements) {
        if(advancements.length == 0) advancements = this.advancements.values().toArray(new CrazyAdvancement[0]);

        CruxJson cfg = getSaveFile(plugin, uuid);
        cfg.reloadIfNeeded();
        JsonObject json = cfg.json();
        JsonRegistry registry = cfg.jsonRegistry();
        JsonObject values = new JsonObject();
        JsonContext ctx = new JsonContext(registry);
        for(CrazyAdvancement a : advancements){
            CruxAdvancementProgress progress = a.getProgressIfPresent(uuid);
            ObjectiveProgression objectiveProgression = a.getObjectiveProgressIfPresent(uuid);
            if(progress==null && objectiveProgression == null) continue;

            JsonObject completeProgress = new JsonObject();
            if(progress != null){
                completeProgress.add("progress", CruxConfigHook.CRUX_ADVANCEMENT_PROGRESS.serializeToJson(ctx, progress));
            }
            if(objectiveProgression != null && !a.isGranted(uuid)){
                completeProgress.add("objective_progress",
                    CruxConfigHook.SIMPLE_OBJECTIVE_PROGRESSION.serializeToJson(ctx, (SimpleObjectiveProgression) objectiveProgression));
            }

            values.add(a.key().asString(), completeProgress);
        }
        json.add("values", values);
        cfg.save(true);
    }

    @Override
    public void loadProgress(@NotNull UUID uuid, @NotNull CrazyAdvancement... advancements) {
        if(advancements.length == 0) advancements = this.advancements.values().toArray(new CrazyAdvancement[0]);

        CruxJson cfg = getSaveFile(plugin, uuid);
        if(!cfg.file().exists()) return;
        JsonObject json = cfg.json();
        if(json==null) return;
        if(!(json.get("values") instanceof JsonObject values)){
            cfg.close();
            return;
        }
        cfg.close();
        JsonRegistry registry = cfg.jsonRegistry();

        JsonContext ctx = new JsonContext(registry);
        for(CrazyAdvancement a : advancements){
            if(!(values.get(a.key().asString()) instanceof JsonObject o)) continue;
            JsonElement ele = o.get("progress");
            if(ele != null){
                CruxAdvancementProgress progress = FileCruxAdvancementProgress.deserialize(
                    ctx,
                    FileElement.fromJson(ele),
                    a.getCriteria()
                );
                if(progress!=null){
                    a.setProgress(uuid, progress);
                    loadCrazyProgress(uuid, getOrCreateCrazyAdvancement(a), progress);
                }
            }
            ele = o.get("objective_progress");
            if(ele != null && a instanceof ObjectiveAdvancement op){
                SimpleObjectiveProgression objectiveProgression = FileSimpleObjectiveProgression.deserialize(
                    ctx, FileElement.fromJson(ele), op
                );
                if(objectiveProgression == null) continue;
                a.setObjectiveProgress(uuid, objectiveProgression);
            }
        }
    }
}
