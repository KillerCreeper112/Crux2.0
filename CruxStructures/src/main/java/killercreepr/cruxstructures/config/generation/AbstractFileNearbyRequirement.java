package killercreepr.cruxstructures.config.generation;

import killercreepr.crux.util.CruxObjects;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.requirement.AbstractStructureNearbyRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFileNearbyRequirement<T extends AbstractStructureNearbyRequirement> extends PureYamlFileHandler<T> {
    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Integer range = o.getObject(Integer.class, "range");
        Integer min = o.getObject(Integer.class, "min");
        Integer max = o.getObject(Integer.class, "max");
        if(CruxObjects.checkNull(range, min)) return null;
        NumberProvider yOffset = registry.deserializeFromFile(NumberProvider.class, o.get("y_offset"));
        return parse(ctx, o, range, min, max, yOffset);
    }

    public abstract @Nullable T parse(@NotNull FileContext<?> ctx, @NotNull FileObject o,
                             Integer range, Integer min, Integer max, NumberProvider yOffset);
}