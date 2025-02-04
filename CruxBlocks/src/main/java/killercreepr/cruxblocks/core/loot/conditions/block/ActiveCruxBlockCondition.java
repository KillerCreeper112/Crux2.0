package killercreepr.cruxblocks.core.loot.conditions.block;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveCruxBlockCondition extends BaseCondition {
    protected final @Nullable Key type;
    public ActiveCruxBlockCondition(@NotNull String target, @Nullable Key type) {
        super(target);
        this.type = type;
    }

    public @Nullable Key getType() {
        return type;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object object = ctx.info().get(target);
        ActiveCruxBlock state;
        if(object instanceof ActiveCruxBlock s) state = s;
        else return false;
        if(type != null && !type.equals(state.getCruxBlock().key())) return false;
        return true;
    }
}