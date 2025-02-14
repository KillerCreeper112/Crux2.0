package killercreepr.cruxattributes.core.config.handler;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCruxAttributeOperation implements FileObjectHandler<CruxAttribute.Operation> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxAttribute.Operation o) {
        return new FilePrimitive(o.id());
    }

    @Override
    public @Nullable CruxAttribute.Operation deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        return CruxAttribute.Operation.match(e.getAsString());
    }
}
