package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IfTargetCondition extends BaseCondition{
    protected final @NotNull String targetType;
    protected final @Nullable LootCondition ifTrueCondition;

    public IfTargetCondition(@NotNull String target, @NotNull String targetType, @Nullable LootCondition ifTrueCondition) {
        super(target);
        this.targetType = targetType;
        this.ifTrueCondition = ifTrueCondition;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object targetObject = ctx.info().get(target);
        if(targetObject==null) return false;
        if(TargetCheckCondition.checkType(targetObject, targetType)){
            return ifTrueCondition == null || ifTrueCondition.test(ctx);
        }
        return false;
    }
}
