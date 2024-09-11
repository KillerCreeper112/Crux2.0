package killercreepr.cruxstructures.config.module;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.module.standard.WallsModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileWall extends PureYamlFileHandler<WallsModule.Wall> {
    @Override
    public @Nullable WallsModule.Wall deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        NumberProvider spacing = registry.deserializeFromFile(NumberProvider.class, o.get("spacing"));
        if(spacing==null) return null;
        List<WallsModule.WallPart> parts = registry.deserializeFromFile(
            new TypeToken<List<WallsModule.WallPart>>(){}.getType(), o.get("parts")
        );
        if(parts == null || parts.isEmpty()) return null;

        return new WallsModule.Wall(parts, spacing);
    }
}