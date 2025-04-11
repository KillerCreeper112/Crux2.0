package killercreepr.crux.core.loot.conditions.evaluation;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EvaluationCondition extends BaseCondition {
    protected final @NotNull String eva;
    //protected final @Nullable Map<String, String> targetPrefixes;
    public EvaluationCondition(@NotNull String target, @NotNull String eva) {
        super(target);
        this.eva = eva;
        //this.targetPrefixes = targetPrefixes;
    }


    @Override
    public boolean test(@NotNull LootContext ctx) {
        MergedTagContainer tags = TagContainer.merged()
            .hookAllWithPrefix(ctx.info());
        /*ctx.info().asMap().forEach((id, holder) ->{
            Object o = holder.value();
            if(o==null) return;
            String prefix = targetPrefixes == null ? null : targetPrefixes.get(id);
            tags.hook(o, prefix == null ? null : TagsPrefixBuilder.overwriteBase(prefix));
        });*/
        return CruxString.parseBoolean(CruxMath.evaluateEvalEx(Crux.format().deserializeString(eva, tags)));
    }
}
