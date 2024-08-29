package killercreepr.cruxstructures.config;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class FileCfgFAWEStructure extends PureYamlFileHandler<CfgFAWEStructure> {
    @Override
    public @Nullable CfgFAWEStructure deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        String schematic = registry.deserializeFromFile(String.class, o.get("schematic"));
        if(schematic==null) return null;
        Boolean persist = registry.deserializeFromFile(Boolean.class, o.get("persistent"));
        Collection<StructureModule> modules = registry.deserializeFromFile(
            new TypeToken<Collection<StructureModule>>(){}.getType(), o.get("modules")
        );
        return new CfgFAWEStructure(
            key, schematic, persist != null && persist, modules == null ? Set.of() : modules
        );
    }
}
