package killercreepr.crux.core.loot.conditions.block;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.paper.block.BukkitStateCruxedBlock;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockStateCondition extends BaseCondition {
    protected final @Nullable BlockPredicate type;
    protected final @Nullable NumberProvider age;
    public BlockStateCondition(@NotNull String target, @Nullable BlockPredicate type, @Nullable NumberProvider age) {
        super(target);
        this.type = type;
        this.age = age;
    }

    public @Nullable BlockPredicate getType() {
        return type;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object object = ctx.info().get(target);
        BlockState state;
        if(object instanceof BlockState s) state = s;
        else if(object instanceof Block s) state = s.getState();
        else return false;
        if(type != null && !type.test(new BukkitStateCruxedBlock(state))) return false;
        //if(type != null && !type.equals(Crux.handlers().block().getType(state))) return false;
        if(age != null){
            if(!(state.getBlockData() instanceof Ageable ageable)) return false;
            if(age.sample(InputContext.inputContext(TagContainer.string().hook(state))).intValue() != ageable.getAge()) return false;
            //if(ageable.getAge() != age) return false;
        }
        return true;
    }
}
