package killercreepr.cruxpotions.config;

import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.bukkit.file.JsonCfg;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.SimpleJsonRegistry;
import killercreepr.cruxpotions.potions.ActivePotion;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class PlayerDataFile extends JsonCfg {
    public final CfgValue<PotionEffect> POTION = new CommonValue<>(
            new PotionEffect(PotionEffectType.HEALTH_BOOST, 100, 1)
    );
    public PlayerDataFile(@NotNull Plugin plugin, @NotNull UUID uuid) {
        super(plugin, uuid.toString());
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public PlayerDataFile(@NotNull File file) {
        super(file);
    }

    public PlayerDataFile(@NotNull CruxJson cfg) {
        super(cfg);
    }

    public PlayerDataFile(@NotNull File file, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(file, jsonRegistry, reloadIfExists);
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry, boolean reloadIfExists) {
        super(plugin, path, jsonRegistry, reloadIfExists);
    }

    public PlayerDataFile(@NotNull File file, @NotNull JsonRegistry jsonRegistry) {
        super(file, jsonRegistry);
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path, @NotNull JsonRegistry jsonRegistry) {
        super(plugin, path, jsonRegistry);
    }

    public PlayerDataFile(@NotNull File file, boolean reloadIfExists) {
        super(file, reloadIfExists);
    }

    public PlayerDataFile(@NotNull Plugin plugin, @NotNull String path, boolean reloadIfExists) {
        super(plugin, path, reloadIfExists);
    }

    public @NotNull Collection<ActivePotion> getPotions(){
        if(true) return null;
        Collection<?> list = null;//cfg.getList("potions");
        try{ return (Collection<ActivePotion>) list; }
        catch (Exception ignored){ ignored.printStackTrace(); }
        return new HashSet<>();
    }

    public PlayerDataFile savePotions(@Nullable Collection<ActivePotion> potions){
        //cfg.set("potions", potions == null ? null : new ArrayList<>(potions));
        return this;
    }
}
