package killercreepr.crux.item.dynamic;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

public interface DynamicItemComponent {
    @NotNull String name();
    void apply(@NotNull CruxItem item, @NotNull TextParserContext context);
}
