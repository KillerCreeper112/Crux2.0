package killercreepr.crux.loot.impl.conditions.evaluation;

import killercreepr.crux.Crux;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.impl.conditions.BaseCondition;
import killercreepr.crux.tags.TagsPrefixBuilder;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EvaluationCondition extends BaseCondition {
    protected final @NotNull String eva;
    protected final @Nullable Map<String, String> targetPrefixes;
    public EvaluationCondition(@NotNull String target, @NotNull String eva, @Nullable Map<String, String> targetPrefixes) {
        super(target);
        this.eva = eva;
        this.targetPrefixes = targetPrefixes;
    }


    @Override
    public boolean test(@NotNull LootContext ctx) {
        MergedTagContainer tags = TagContainer.merged();
        ctx.info().asMap().forEach((id, holder) ->{
            Object o = holder.value();
            if(o==null) return;
            String prefix = targetPrefixes == null ? null : targetPrefixes.get(id);
            tags.hook(o, prefix == null ? null : TagsPrefixBuilder.overwriteBase(prefix));
        });
        return CruxString.parseBoolean(CruxMath.evaluateEvalEx(Crux.format().deserializeString(eva, tags)));
    }
}
