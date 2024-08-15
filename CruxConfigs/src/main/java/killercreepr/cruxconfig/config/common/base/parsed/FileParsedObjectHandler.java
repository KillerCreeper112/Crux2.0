package killercreepr.cruxconfig.config.common.base.parsed;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileParsedObjectHandler<T> extends Keyed {
    /**
     *
     * @return The priority of this handler.
     * Lower values mean this will get parsed sooner basically meaning less priority.
     */
    int getPriority();
    @NotNull Class<T> getTargetType();
    @Nullable T parse(@NotNull FileElement from, @NotNull FileContext<?> context, @NotNull T original, @Nullable T current);
}
