package killercreepr.cruxstructures.core.config;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import killercreepr.cruxstructures.core.structure.CfgFAWEStructure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileCfgFAWEStructure extends PureYamlFileHandler<CfgFAWEStructure> {
    @Override
    public @Nullable CfgFAWEStructure deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) return null;
        return deserializeFromFile(ctx, e, key);
    }

    public @Nullable CfgFAWEStructure deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull Key key) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        String schematic = registry.deserializeFromFile(String.class, o.get("schematic"));
        if(schematic==null) return null;
        Boolean persist = registry.deserializeFromFile(Boolean.class, o.get("persistent"));
        List<StructureComponent> beforePlacementModules = registry.deserializeFromFile(
            new TypeToken<List<StructureComponent>>(){}.getType(), o.get("before_modules")
        );
        List<StructureComponent> modules = registry.deserializeFromFile(
            new TypeToken<List<StructureComponent>>(){}.getType(), o.get("modules")
        );
        CfgFAWEStructure loaded = new CfgFAWEStructure(
            key, schematic, persist != null && persist, beforePlacementModules, modules == null ? List.of() : modules
        );
        if(modules != null){
            modules.forEach(module ->{
                if(!(module instanceof StructureComponent c)) return;
                c.onStructureHook(loaded);
            });
        }
        return loaded;
    }
}
