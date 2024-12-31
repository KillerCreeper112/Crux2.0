package killercreepr.cruxconfig.config.common.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;

/**
 * Mainly used just so it's easier for future compatibility.
 */
@Deprecated
public abstract class PureYamlFileHandler<T> extends SimpleFileHandler<T> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        throw new RuntimeException(object.getClass() + " does not have a serialize method set!");
    }

    @Override
    public @NotNull String jsonSerializerID() {
        throw new RuntimeException(getClass().getSimpleName() + " does not have a json serializer ID set.");
    }
}
