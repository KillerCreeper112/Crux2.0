package killercreepr.cruxexternal;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
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
    public void onLoad(@NotNull CruxPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        if(pm.getPlugin("HeadDatabase") != null){
            plugin.log("Using HeadDatabase as a Crux SkullProvider!");
            Crux.handlers().setSkullProvider(new HeadDataBaseSkullProvider());
        }
    }
}
