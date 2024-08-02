package killercreepr.cruxadvancements.crazy;

import com.google.gson.JsonObject;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.event.*;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CfgCrazyAdvancementManager extends CrazyAdvancementManager<CrazyAdvancement> {
    public static CfgCrazyAdvancementManager createNew(@NotNull Key key, @NotNull Plugin plugin){
        return new CfgCrazyAdvancementManager(key, new AdvancementManager(CrazyUtil.toNameKey(key)), plugin);
    }

    protected final @NotNull Plugin plugin;
    public CfgCrazyAdvancementManager(@NotNull Key key, @NotNull AdvancementManager crazyManager, @NotNull Plugin plugin) {
        super(key, crazyManager);
        this.plugin = plugin;
    }

    @Override
    public @Nullable CruxAdvancementGrantEvent grantAdvancement(@NotNull UUID who, @NotNull CrazyAdvancement advancement) {
        CruxAdvancementGrantEvent event = super.grantAdvancement(who, advancement);
        if(event==null || event.isCancelled()) return event;
        crazyManager.grantAdvancement(who, getOrCreateCrazyAdvancement(advancement));
        return event;
    }

    @Override
    public @Nullable CruxAdvancementRevokeEvent revokeAdvancement(@NotNull UUID who, @NotNull CrazyAdvancement advancement) {
        CruxAdvancementRevokeEvent event = super.revokeAdvancement(who, advancement);
        if(event == null || event.isCancelled()) return event;
        crazyManager.revokeAdvancement(who, getOrCreateCrazyAdvancement(advancement));
        return event;
    }

    @Override
    public @Nullable CruxAdvancementCriteriaGrantEvent grantCriteria(@NotNull UUID who, @NotNull CrazyAdvancement advancement, @NotNull String... criteria) {
        CruxAdvancementCriteriaGrantEvent event = super.grantCriteria(who, advancement, criteria);
        if(event==null || event.isCancelled()) return event;
        crazyManager.grantCriteria(who, getOrCreateCrazyAdvancement(advancement), criteria);
        return event;
    }

    @Override
    public @Nullable CruxAdvancementCriteriaRevokeEvent revokeCriteria(@NotNull UUID who, @NotNull CrazyAdvancement advancement, @NotNull String... criteria) {
        CruxAdvancementCriteriaRevokeEvent event = super.revokeCriteria(who, advancement, criteria);
        if(event==null || event.isCancelled()) return event;
        crazyManager.revokeCriteria(who, getOrCreateCrazyAdvancement(advancement), criteria);
        return event;
    }

    @Override
    public @Nullable CruxAdvancementProgressChangeEvent setCriteriaProgress(@NotNull UUID who, @NotNull CrazyAdvancement advancement, int newProgress) {
        CruxAdvancementProgressChangeEvent event = super.setCriteriaProgress(who, advancement, newProgress);
        if(event==null || event.isCancelled()) return event;
        crazyManager.setCriteriaProgress(who, getOrCreateCrazyAdvancement(advancement), newProgress);
        return event;
    }

    public @NotNull CruxFolder getAdvancementsFolder(@NotNull Plugin plugin){
        return new CruxFolder(plugin, "advancements/" + key.asString().replace(":", "_"));
    }

    public @NotNull CruxJson getSaveFile(@NotNull Plugin plugin, @NotNull UUID player){
        return new CruxJson(plugin, "advancements/" + key.asString().replace(":", "_") +
            "/data/" + player);
    }

    public @NotNull Collection<CrazyAdvancement>  parseAdvancements(@NotNull Plugin plugin){
        File[] files = getAdvancementsFolder(plugin).file().listFiles();
        if(files==null) return Set.of();
        Collection<CrazyAdvancement> list = new HashSet<>();
        for(File f : files){
            if(!CruxFolder.hasFileExtension(f, "yml")) continue;
            CruxConfig cfg = new CruxConfig(f);
            CrazyAdvancement advancement = cfg.deserialize(CrazyAdvancement.class, "");
            if(advancement==null) continue;
            list.add(advancement);
        }
        return list;
    }

    @Override
    public void save(@NotNull Plugin plugin) {

    }

    @Override
    public void load(@NotNull Plugin plugin) {
        for(CrazyAdvancement a : parseAdvancements(plugin)){
            registerAdvancement(a);
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
        for(CrazyAdvancement a : advancements){
            CruxAdvancementProgress progress = a.getProgressIfPresent(uuid);
            if(progress==null) continue;
            values.add(a.key().asString(), registry.serializeObject(progress));
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
        if(!(json.get("values") instanceof JsonObject values)) return;
        JsonRegistry registry = cfg.jsonRegistry();

        for(CrazyAdvancement a : advancements){
            CruxAdvancementProgress progress = registry.deserialize(CruxAdvancementProgress.class, values.get(a.key().asString()));
            if(progress==null) continue;
            a.setProgress(uuid, progress);
        }
    }
}
