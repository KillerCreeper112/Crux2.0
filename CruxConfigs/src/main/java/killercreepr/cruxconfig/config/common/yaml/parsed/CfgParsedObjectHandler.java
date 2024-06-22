package killercreepr.cruxconfig.config.common.yaml.parsed;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CfgParsedObjectHandler<T> extends Keyed {
    /**
     *
     * @return The priority of this handler.
     * Lower values mean this will get parsed sooner basically meaning less priority.
     */
    int getPriority();
    @NotNull Class<T> getTargetType();
    @Nullable T parse(@NotNull YamlElement from, @NotNull YamlContext context, @NotNull T original, @Nullable T current);
}
