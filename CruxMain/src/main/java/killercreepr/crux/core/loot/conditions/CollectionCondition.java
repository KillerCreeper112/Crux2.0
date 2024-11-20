package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.data.DataExchange;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CollectionCondition extends BaseCondition{
    protected final @NotNull LootCondition condition;
    protected final boolean anyOf;

    public CollectionCondition(@NotNull String target, @NotNull LootCondition condition, @NotNull String type) {
        super(target);
        this.condition = condition;
        this.anyOf = type.equalsIgnoreCase("any_of");
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object object = ctx.info().get(target);
        if(object == null) return false;
        if(object instanceof Collection<?> list){
            if(anyOf){
                for(Object o : list){
                    if(test(ctx, o)) return true;
                }
                return false;
            }
            for(Object o : list){
                if(!test(ctx, o)) return false;
            }
            return true;
        }
        return test(ctx, object);
    }

    public boolean test(@NotNull LootContext ctx, @NotNull Object value){
        LootContext newCtx = ctx.withInfo(
            DataExchange.builder().putAll(ctx.info()).put("this", value).build()
        );
        return condition.test(newCtx);
    }
}
