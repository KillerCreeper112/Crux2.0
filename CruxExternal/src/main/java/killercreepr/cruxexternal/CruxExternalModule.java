package killercreepr.cruxexternal;

import killercreepr.crux.Crux;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxexternal.headdatabase.HeadDataBaseSkullProvider;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class CruxExternalModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_EXTERNAL;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        if(pm.getPlugin("HeadDataBase") != null){
            Crux.handlers().setSkullProvider(new HeadDataBaseSkullProvider());
        }
    }
}
