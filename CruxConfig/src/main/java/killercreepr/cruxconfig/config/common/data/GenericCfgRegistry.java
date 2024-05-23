package killercreepr.cruxconfig.config.common.data;

import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.cruxconfig.config.common.file.ICfg;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GenericCfgRegistry<T extends ICfg<?, ?>> extends SimpleRegistry<T> {
    public GenericCfgRegistry(@NotNull Collection<T> collection) {
        super(collection);
    }

    public void reload(){
        this.forEach(ICfg::setup);
    }
}
