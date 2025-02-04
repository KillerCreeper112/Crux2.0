package killercreepr.cruxblocks.core.loot.conditions.block;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxBlockCondition extends BaseCondition {
    protected final @Nullable Key type;
    public CruxBlockCondition(@NotNull String target, @Nullable Key type) {
        super(target);
        this.type = type;
    }

    public @Nullable Key getType() {
        return type;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object object = ctx.info().get(target);
        CruxBlock state;
        if(object instanceof CruxBlock s) state = s;
        else if(object instanceof ActiveCruxBlock s) state = s.getCruxBlock();
        else return false;
        if(type != null && !type.equals(state.key())) return false;
        return true;
    }
}