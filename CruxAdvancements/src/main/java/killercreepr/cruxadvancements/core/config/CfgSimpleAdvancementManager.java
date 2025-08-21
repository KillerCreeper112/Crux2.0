package killercreepr.cruxadvancements.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.util.Pair;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.manager.SimpleAdvancementManager;
import killercreepr.cruxadvancements.core.advancement.objective.GlobalObjectiveAdvancement;
import killercreepr.cruxadvancements.core.advancement.objective.progress.SimpleObjectiveProgression;
import killercreepr.cruxadvancements.core.config.handler.FileCruxAdvancementProgress;
import killercreepr.cruxadvancements.core.config.handler.FileSimpleObjectiveProgression;
import killercreepr.cruxadvancements.core.config.loader.ObjectiveAdvancementCfgLoader;
import killercreepr.cruxadvancements.core.data.TrackedAdvancement;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
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
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class CfgSimpleAdvancementManager extends SimpleAdvancementManager<ObjectiveAdvancement> {
    public static CfgSimpleAdvancementManager createNew(@NotNull Key key, @NotNull CruxPlugin plugin, String fileLoadingPath){
        return new CfgSimpleAdvancementManager(key, plugin, fileLoadingPath);
    }

    protected final @NotNull CruxPlugin plugin;
    protected final String fileLoadingPath;
    public CfgSimpleAdvancementManager(@NotNull Key key, @NotNull CruxPlugin plugin, String fileLoadingPath) {
        super(key);
        this.plugin = plugin;
        this.fileLoadingPath = fileLoadingPath;
    }

    public @NotNull CruxFolder getAdvancementsFolder(@NotNull Plugin plugin){
        return new CruxFolder(plugin, fileLoadingPath + "/" + key.value());
    }

    public @NotNull CruxJson getSaveFile(@NotNull Plugin plugin, @NotNull UUID player){
        return getSaveFile(plugin, player.toString());
    }

    public @NotNull CruxJson getSaveFile(@NotNull Plugin plugin, @NotNull String player){
        return new CruxJson(plugin, "data/cruxadvancements/" + key.asString().replace(":", "_") +
            "/" + player);
    }

    public void forEachSaveFile(@NotNull Plugin plugin, @NotNull Consumer<File> consumer){
        File[] files = CruxFolder.file(plugin, "data/cruxadvancements/" + key.asString().replace(":", "_"))
            .listFiles();
        if(files == null) return;
        for(File f : files){
            if(!f.getName().endsWith(".json")) continue;
            consumer.accept(f);
        }
    }

    public @NotNull Collection<ObjectiveAdvancement> parseAdvancements(@NotNull File folder){
        Collection<ObjectiveAdvancement> list = new HashSet<>();
        new ObjectiveAdvancementCfgLoader(plugin, list::add).loadConfiguration(folder, key().value());
        return list;
    }

    @Override
    public void save(@NotNull Plugin plugin) {

    }

    @Override
    public void refresh(@NotNull Plugin plugin) {
        refresh(plugin, null);
    }

    public void refresh(@NotNull Plugin plugin, Consumer<CruxAdvancementManager<?>> loadConsumer) {
        for(Player p : plugin.getServer().getOnlinePlayers()){
            saveProgress(p.getUniqueId());
        }
        for(ObjectiveAdvancement a : new HashSet<>(advancements.values())){
            unregisterAdvancement(a);
        }
        load(plugin, loadConsumer);

        for(Player p : plugin.getServer().getOnlinePlayers()){
            loadProgress(p.getUniqueId());
        }
    }

    @Override
    public void load(@NotNull Plugin plugin) {
        load(plugin, null);
    }

    public void load(@NotNull Plugin plugin, Consumer<CruxAdvancementManager<?>> loadConsumer) {
        for(ObjectiveAdvancement a : parseAdvancements(getAdvancementsFolder(plugin).file())){
            registerAdvancement(a);

            if(a instanceof GlobalObjectiveAdvancement){
                TrackedAdvancement tracked = new TrackedAdvancement(key(), a.key(), true, System.currentTimeMillis());
                AdvancementRegistries.GLOBAL_ADVANCEMENTS.register(tracked);
            }

            Crux.log(Level.INFO, "Registered ObjectiveAdvancement: " + key() + " -> " + a.key());
        }
        load();
        if(loadConsumer != null) loadConsumer.accept(this);
    }

    @Override
    public void saveProgress(@NotNull UUID uuid, @NotNull ObjectiveAdvancement... advancements) {
        /*List<ObjectiveAdvancement> advList;
        if (advancements.length == 0) {
            advList = new ArrayList<>(this.advancements.values());
        } else {
            advList = Arrays.asList(advancements);
        }*/
        if(advancements.length == 0) advancements = this.advancements.values().toArray(new ObjectiveAdvancement[0]);

        CruxJson cfg = getSaveFile(plugin, uuid);
        cfg.reloadIfNeeded();
        JsonObject json = cfg.json();
        JsonRegistry registry = cfg.jsonRegistry();
        JsonObject values = new JsonObject();
        JsonContext ctx = new JsonContext(registry);
        for(ObjectiveAdvancement a : advancements){
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
        cfg.save();
    }

    @Override
    public void loadProgress(@NotNull UUID uuid, @NotNull ObjectiveAdvancement... advancements) {
        if(advancements.length == 0) advancements = this.advancements.values().toArray(new ObjectiveAdvancement[0]);
        //List<ObjectiveAdvancement> advList;
        /*if (advancements.length == 0) {
            advList = new ArrayList<>(this.advancements.values());
        } else {
            advList = Arrays.asList(advancements);
        }*/

        CruxJson cfg = getSaveFile(plugin, uuid);
        if(!cfg.file().exists()){
            cfg.close();
            return;
        }
        JsonObject json = cfg.json();
        if(json==null){
            cfg.close();
            return;
        }
        if(!(json.get("values") instanceof JsonObject values)){
            cfg.close();
            return;
        }
        cfg.close();
        JsonRegistry registry = cfg.jsonRegistry();

        JsonContext ctx = new JsonContext(registry);
        for(ObjectiveAdvancement a : advancements){
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

    @Override
    public void saveAllUserProgress(@NotNull ObjectiveAdvancement... advancements) {
        if(advancements.length == 0) advancements = this.advancements.values().toArray(new ObjectiveAdvancement[0]);

        Map<String, Pair<CruxJson, JsonObject>> loaded = new HashMap<>();
        for(ObjectiveAdvancement a : advancements){
            a.getProgressMap().keySet().forEach(name ->{
                var loadedAlready = loaded.get(name);
                CruxJson cfg;
                JsonObject values;
                if(loadedAlready == null){
                    cfg = getSaveFile(plugin, name);
                    cfg.reloadIfNeeded();
                    values = new JsonObject();
                    loaded.put(name, Pair.of(cfg, values));
                }else{
                    cfg = loadedAlready.getFirst();
                    values = loadedAlready.getSecond();
                }
                JsonRegistry registry = cfg.jsonRegistry();
                JsonContext ctx = new JsonContext(registry);

                CruxAdvancementProgress progress = a.getProgressIfPresent(name);
                ObjectiveProgression objectiveProgression = a.getObjectiveProgressIfPresent(name);
                if(progress==null && objectiveProgression == null) return;

                JsonObject completeProgress = new JsonObject();
                if(progress != null){
                    completeProgress.add("progress", CruxConfigHook.CRUX_ADVANCEMENT_PROGRESS.serializeToJson(ctx, progress));
                }
                if(objectiveProgression != null && !a.isGranted(name)){
                    completeProgress.add("objective_progress",
                        CruxConfigHook.SIMPLE_OBJECTIVE_PROGRESSION.serializeToJson(ctx, (SimpleObjectiveProgression) objectiveProgression));
                }
                values.add(a.key().asString(), completeProgress);
            });
        }
        loaded.forEach((name, pair) ->{
            CruxJson cfg = pair.getFirst();
            JsonObject values = pair.getSecond();
            cfg.json().add("values", values);
            cfg.save();
        });
    }

    @Override
    public void loadAllUserProgress(@NotNull ObjectiveAdvancement... advancements) {
        if(advancements.length == 0) advancements = this.advancements.values().toArray(new ObjectiveAdvancement[0]);
        /*List<ObjectiveAdvancement> advList;
        if (advancements.length == 0) {
            advList = new ArrayList<>(this.advancements.values());
        } else {
            advList = Arrays.asList(advancements);
        }*/

        @NotNull ObjectiveAdvancement[] finalAdvancements = advancements;
        forEachSaveFile(plugin, file ->{
            CruxJson cfg = new CruxJson(file);
            JsonObject json = cfg.json();
            if(json==null){
                cfg.close();
                return;
            }
            if(!(json.get("values") instanceof JsonObject values)){
                cfg.close();
                return;
            }
            cfg.close();
            JsonRegistry registry = cfg.jsonRegistry();
            JsonContext ctx = new JsonContext(registry);

            String uuid = CruxFolder.withoutFileExtension(file.getName());
            for(ObjectiveAdvancement a : finalAdvancements){
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
        });
    }
}
