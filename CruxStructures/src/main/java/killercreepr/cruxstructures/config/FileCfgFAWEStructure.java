package killercreepr.cruxstructures.config;

import killercreepr.cruxconfig.config.bukkit.handler.YamlFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCfgFAWEStructure extends YamlFileHandler<CfgFAWEStructure> {
    @Override
    public @Nullable CfgFAWEStructure deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserialize(Key.class, o.get("key"));
        if(key==null) return null;
        String schematic = registry.deserialize(String.class, o.get("schematic"));
        if(schematic==null) return null;
        Boolean persist = registry.deserialize(Boolean.class, o.get("persistent"));
        return new CfgFAWEStructure(
            key, schematic, persist != null && persist
        );
    }
}
