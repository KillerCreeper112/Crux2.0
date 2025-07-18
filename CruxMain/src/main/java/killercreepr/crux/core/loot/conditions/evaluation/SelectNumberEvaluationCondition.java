package killercreepr.crux.core.loot.conditions.evaluation;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.util.CruxMath;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class SelectNumberEvaluationCondition extends BaseCondition {
    protected final Object match;
    protected final String eva;
    protected final Collection<String> targets;
    protected final String operation;

    public SelectNumberEvaluationCondition(@NotNull String target, Object match, String eva, Collection<String> targets, String operation) {
        super(target);
        this.match = match;
        this.eva = eva;
        this.targets = targets;
        this.operation = operation;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        var tags = TagContainer.string();

        if(targets == null || targets.isEmpty()){
            tags.hookAllWithPrefix(ctx.info());
        }else{
            for(String s : targets){
                Object object = ctx.info().get(s);
                if(object == null) continue;
                tags.hook(object, TagsPrefixBuilder.overwriteBase(s + "_"));
            }
        }

        String check = Crux.format().deserializeString(eva, tags);
        double actual;
        try{
            actual = CruxMath.evaluate(check);
        }catch (Exception e){ return false; }

        double value;
        if(match instanceof Number n){
            value = n.doubleValue();
        }else{
            try{
                value = CruxMath.evaluate(Crux.format().deserializeString(match.toString(), tags));
            }catch (Exception e){ return false; }
        }

        return switch (operation) {
            case "==" -> actual == value;
            case "!=" -> actual != value;
            case ">"  -> actual > value;
            case ">=" -> actual >= value;
            case "<"  -> actual < value;
            case "<=" -> actual <= value;
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
        //return true;
    }
}
