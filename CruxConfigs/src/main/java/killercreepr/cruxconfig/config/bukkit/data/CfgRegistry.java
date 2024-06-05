package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.common.data.GenericCfgRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CfgRegistry extends GenericCfgRegistry<Cfg> {
    public CfgRegistry(@NotNull Collection<Cfg> collection) {
        super(collection);
    }
}
