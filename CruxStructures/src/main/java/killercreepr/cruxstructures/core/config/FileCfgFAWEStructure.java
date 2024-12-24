package killercreepr.cruxstructures.core.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.core.structure.CfgFAWEStructure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

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

        DataComponentHandler handler = registry.deserializeFromFile(
            DataComponentHandler.class, o.get("components")
        );
        /*List<TypedDataComponent<?>> components = registry.deserializeFromFile(
            new TypeToken<List<TypedDataComponent>>(){}.getType(), o.get("components")
        );*/

        /*List<TypedDataComponent<?>> components = registry.deserializeFromFile(
            new TypeToken<List<TypedDataComponent<?>>>(){}.getType(), o.get("components")
        );
        List<TypedDataComponent<?>> beforeComponents = registry.deserializeFromFile(
            new TypeToken<List<TypedDataComponent<?>>>(){}.getType(), o.get("before_components")
        );*/
        /*List<StructureComponent> modules = new ArrayList<>();
        if(components != null){
            for(TypedDataComponent<?> typed : components){
                if(typed.getValue() instanceof StructureComponent s) modules.add(s);
            }
        }
        List<StructureComponent> beforeModules;
        if(beforeComponents == null || beforeComponents.isEmpty()){
            beforeModules = null;
        }else{
            beforeModules = new ArrayList<>();
            for(TypedDataComponent<?> typed : beforeComponents){
                if(typed.getValue() instanceof StructureComponent s) modules.add(s);
            }
        }*/
        CfgFAWEStructure loaded = new CfgFAWEStructure(
            key, schematic, persist != null && persist, beforePlacementModules, modules == null ? List.of() : modules
        );
        if(handler != null){
            Crux.log(Level.WARNING, "loading compoents " + handler.getAllOfType(Object.class) + "    in " + loaded.key());
            handler.forEach(loaded::set);
        }
        loaded.load();
        return loaded;
    }
}
