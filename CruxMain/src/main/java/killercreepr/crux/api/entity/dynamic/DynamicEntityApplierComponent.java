package killercreepr.crux.api.entity.dynamic;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.NotNull;

public interface DynamicEntityApplierComponent {
    @NotNull String name();
    void apply(@NotNull CruxEntity entity, @NotNull TextParserContext context);
}
