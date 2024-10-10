package killercreepr.crux.loot.impl.conditions.block;

import killercreepr.crux.Crux;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.impl.conditions.BaseCondition;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockStateCondition extends BaseCondition {
    protected final @Nullable Key type;
    protected final @Nullable Integer age;
    public BlockStateCondition(@NotNull String target, @Nullable Key type, @Nullable Integer age) {
        super(target);
        this.type = type;
        this.age = age;
    }

    public @Nullable Key getType() {
        return type;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object object = ctx.info().get(target);
        BlockState state;
        if(object instanceof BlockState s) state = s;
        else if(object instanceof Block s) state = s.getState();
        else return false;
        if(type != null && !type.equals(Crux.handlers().block().getType(state.getBlockData()))) return false;
        if(age != null){
            if(!(state.getBlockData() instanceof Ageable ageable)) return false;
            if(ageable.getAge() != age) return false;
        }
        return true;
    }
}
