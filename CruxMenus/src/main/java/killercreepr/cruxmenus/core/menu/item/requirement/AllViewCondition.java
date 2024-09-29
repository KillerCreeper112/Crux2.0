package killercreepr.cruxmenus.core.menu.item.requirement;

import killercreepr.crux.context.TextParserContext;
import killercreepr.cruxmenus.api.menu.item.requirement.ViewCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AllViewCondition implements ViewCondition {
    protected final @NotNull Collection<ViewCondition> requirements;
    public AllViewCondition(@NotNull Collection<ViewCondition> requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean test(@NotNull TextParserContext ctx) {
        for(ViewCondition r : requirements){
            if(!r.test(ctx)) return false;
        }
        return true;
    }
}
