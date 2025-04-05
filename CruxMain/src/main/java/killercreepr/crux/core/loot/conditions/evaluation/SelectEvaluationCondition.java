package killercreepr.crux.core.loot.conditions.evaluation;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import org.jetbrains.annotations.NotNull;

//todo finish this
@Deprecated(forRemoval = true)
public class SelectEvaluationCondition extends BaseCondition {
    protected final String match;
    protected final boolean ignoreCase;
    protected final boolean parseVariables;
    public SelectEvaluationCondition(@NotNull String target, String match, boolean ignoreCase, boolean parseVariables) {
        super(target);
        this.match = match;
        this.ignoreCase = ignoreCase;
        this.parseVariables = parseVariables;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object target = ctx.info().get(this.target);
        String check = target + "";

        if(parseVariables && target != null){
            var tags = TagContainer.string()
                .hookAllWithPrefix(ctx.info());
            check = Crux.format().deserializeString(check, tags);
        }

        if(ignoreCase){
            if(!match.equalsIgnoreCase(check)) return false;
        }else if(!match.equals(check)) return false;
        return true;
    }
}
