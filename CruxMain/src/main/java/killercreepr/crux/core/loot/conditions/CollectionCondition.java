package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class CollectionCondition extends BaseCondition{
    protected final LootCondition condition;
    protected final boolean all;
    protected final boolean useMapKeys;

    public CollectionCondition(@NotNull String target, @NotNull LootCondition condition, @NotNull String type, boolean useMapKeys) {
        super(target);
        this.condition = condition;
        this.all = type.equalsIgnoreCase("all") || type.equalsIgnoreCase("all_of");
        this.useMapKeys = useMapKeys;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object object = ctx.info().get(target);
        if(object == null) return false;
        Collection<?> list;
        if(object instanceof Collection<?> l){
            list = l;
        }else if(object instanceof Map<?,?> map){
            list = useMapKeys ? map.keySet() : map.values();
        }else return condition.test(ctx.withInfo(ctx.info().append(target, Holder.directObject(object))));

        if(all){
            for(Object o : list){
                if(!condition.test(ctx.withInfo(ctx.info().append(target, Holder.directObject(o))))) return false;
            }
            return true;
        }
        for(Object o : list){
            if(condition.test(ctx.withInfo(ctx.info().append(target, Holder.directObject(o))))) return true;
        }
        return false;

        /*Object object = ctx.info().get(target);
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
        return test(ctx, object);*/
    }

    /*public boolean test(@NotNull LootContext ctx, @NotNull Object value){
        LootContext newCtx = ctx.withInfo(
            DataExchange.builder().putAll(ctx.info()).put("this", value).build()
        );
        return condition.test(newCtx);
    }*/
}
