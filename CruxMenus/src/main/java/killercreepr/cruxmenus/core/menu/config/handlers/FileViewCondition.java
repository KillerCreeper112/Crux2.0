package killercreepr.cruxmenus.core.menu.config.handlers;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxmenus.api.menu.item.requirement.ViewCondition;
import killercreepr.cruxmenus.core.menu.item.requirement.AllViewCondition;
import killercreepr.cruxmenus.core.menu.item.requirement.AnyViewCondition;
import killercreepr.cruxmenus.core.menu.item.requirement.SingleViewCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FileViewCondition implements FileObjectHandler<ViewCondition> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull ViewCondition object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ViewCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(e instanceof FileGeneric g){
            return new SingleViewCondition(g.getAsString());
        }
        FileRegistry registry = ctx.getRegistry();
        if(e instanceof FileArray a){
            Collection<ViewCondition> requirements = registry.deserializeFromFile(
                new TypeToken<List<ViewCondition>>(){}.getType(), a
            );
            if(requirements == null || requirements.isEmpty()) return null;
            return new AllViewCondition(requirements);
        }
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "condition");
        if(type == null) return null;
        switch (type.toLowerCase()){
            case "any_of" ->{
                Collection<ViewCondition> requirements = registry.deserializeFromFile(
                    new TypeToken<List<ViewCondition>>(){}.getType(), o.get("terms")
                );
                if(requirements == null || requirements.isEmpty()) return null;
                return new AnyViewCondition(requirements);
            }
            case "all_of" ->{
                return registry.deserializeFromFile(ViewCondition.class, o.get("terms"));
            }
            default -> registry.deserializeFromFile(ViewCondition.class, o.get("terms"));
        }
        return null;
    }
}
