package killercreepr.cruxmenus.api.menu.item.requirement;

import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.NotNull;

public interface ViewCondition {
    boolean test(@NotNull TextParserContext ctx);
}
