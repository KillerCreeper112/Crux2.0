package killercreepr.cruxattributes.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxattributes.api.attribute.CruxAttributeHandler;
import killercreepr.cruxattributes.api.attribute.DynamicCruxAttributeInstance;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class FileCruxAttributeHandler implements FileObjectHandler<CruxAttributeHandler> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxAttributeHandler o) {
        return new FileObject()
            .add("instances", ctx.getRegistry().serializeToFile(o.getInstances()))
            ;
    }

    @Override
    public @Nullable CruxAttributeHandler deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        Collection<DynamicCruxAttributeInstance> instances = reg.deserializeFromFile(
            new TypeToken<Collection<DynamicCruxAttributeInstance>>(){}.getType(), o.get("instances")
        );
        if(instances == null) return null;
        return CruxAttributeHandler.attributeHandler(instances);
    }
}
