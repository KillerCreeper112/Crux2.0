package killercreepr.crux.api.item.dynamic;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.SimpleCruxItem;
import org.jetbrains.annotations.NotNull;

public interface DynamicItemComponent {
    @NotNull String name();
    void apply(@NotNull SimpleCruxItem item, @NotNull TextParserContext context);
    default @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with){
        return this;
    }
}
