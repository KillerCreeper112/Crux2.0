package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CfgRegistry extends SimpleRegistry<Cfg> {
    public CfgRegistry(@NotNull Collection<Cfg> collection) {
        super(collection);
    }

    public void reload(){
        this.forEach(Cfg::setup);
    }
}
