package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CfgCrazyAdvancementManager extends CrazyAdvancementManager<CrazyAdvancement> {
    public static CfgCrazyAdvancementManager createNew(@NotNull Key key){
        return new CfgCrazyAdvancementManager(key, new AdvancementManager(CrazyUtil.toNameKey(key)));
    }

    public CfgCrazyAdvancementManager(@NotNull Key key, @NotNull AdvancementManager crazyManager) {
        super(key, crazyManager);
    }

    public @NotNull CruxFolder getAdvancementsFolder(@NotNull Plugin plugin){
        return new CruxFolder(plugin, "advancements/" + key.asString().replace(":", "_"));
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
        }
        loadAllCrazyAdvancements();
    }
}
