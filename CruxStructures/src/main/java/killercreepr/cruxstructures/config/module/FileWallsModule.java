package killercreepr.cruxstructures.config.module;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.module.standard.WallsModule;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FileWallsModule extends PureYamlFileHandler<WallsModule> {
    @Override
    public @Nullable WallsModule deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Map<BlockFace, StructureGenerator> walls = registry.deserializeFromFile(
            new TypeToken<Map<BlockFace, StructureGenerator>>(){}.getType(), o.get("walls")
        );
        if(walls == null) return null;
        Map<BlockFace, NumberProvider> wallSpacing = registry.deserializeFromFile(
            new TypeToken<Map<BlockFace, NumberProvider>>(){}.getType(), o.get("wall_spacing")
        );

        if(wallSpacing == null) wallSpacing = Map.of();

        WallsModule.WallRotationType rotationType = registry.deserializeFromFile(
            WallsModule.WallRotationType.class, o.get("rotation_type")
        );
        if(rotationType==null) rotationType = WallsModule.WallRotationType.STRUCTURE;

        return new WallsModule(
            walls,
            registry.deserializeFromFile(NumberProvider.class, o.get("default_wall_spacing")),
            wallSpacing,
            rotationType
        );
    }
}
