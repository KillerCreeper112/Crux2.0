package killercreepr.cruxmenus.core.menu.item.requirement;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import killercreepr.cruxmenus.api.menu.item.requirement.ViewCondition;
import org.jetbrains.annotations.NotNull;

public class SingleViewCondition implements ViewCondition {
    protected final @NotNull String text;
    public SingleViewCondition(@NotNull String text) {
        this.text = text;
    }

    @Override
    public boolean test(@NotNull TextParserContext ctx) {
        return CruxString.parseBoolean(
            CruxMath.evaluateEvalEx(ctx.deserializeString(text))
        );
    }
}
