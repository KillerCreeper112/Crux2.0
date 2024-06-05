package killercreepr.cruxconfig.config.common.data;

import killercreepr.cruxconfig.config.common.file.ICfg;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class GenericCfgContainer<T extends ICfg<?, ?>> {
    protected final GenericCfgRegistry<T> registry = new GenericCfgRegistry<>(new HashSet<>());
    public GenericCfgContainer<T> add(@NotNull T... cfgs){
        for(T c : cfgs){
            registry.register(c);
        }
        return this;
    }
    public GenericCfgContainer<T> reload(){
        registry.reload();
        return this;
    }

    public GenericCfgRegistry<T> getRegistry() {
        return registry;
    }
}
