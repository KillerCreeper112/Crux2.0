package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class CfgContainer {
    protected final CfgRegistry registry = new CfgRegistry(new HashSet<>());
    public CfgContainer add(@NotNull Cfg... cfgs){
        for(Cfg c : cfgs){
            registry.register(c);
        }
        return this;
    }
    public CfgContainer reload(){
        registry.reload();
        return this;
    }

    public CfgRegistry getRegistry() {
        return registry;
    }
}
