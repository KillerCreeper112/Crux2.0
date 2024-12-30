package killercreepr.crux.api.item.dynamic;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface DynamicItemComponent extends Cloneable {
    @NotNull String name();
    void apply(@NotNull CruxItem item, @NotNull TextParserContext context);
    default @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with){
        return this;
    }
    DynamicItemComponent clone();
}
