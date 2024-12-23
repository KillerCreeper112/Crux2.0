package killercreepr.cruxstructures.core.config.generation;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.generation.requirement.AbstractStructureDirectionTestRequirement;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class AbstractFileDirectionTestRequirement<T extends AbstractStructureDirectionTestRequirement> extends PureYamlFileHandler<T> {
    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Map<BlockFace, NumberProvider> directions = registry.deserializeFromFile(
            new TypeToken<Map<BlockFace, NumberProvider>>(){}.getType(), o.get("directions")
        );
        NumberProvider minDirectionAmount = registry.deserializeFromFile(NumberProvider.class, o.get("min_direction_amount"));
        if(CruxObjects.checkNull(directions, minDirectionAmount)) return null;
        return parse(ctx, o, directions, minDirectionAmount);
    }

    public abstract @Nullable T parse(@NotNull FileContext<?> ctx, @NotNull FileObject o,
                             @NotNull Map<BlockFace, NumberProvider> directions, @NotNull NumberProvider minDirectionAmount);
}