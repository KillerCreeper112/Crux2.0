package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetCheckCondition extends BaseCondition{
    protected final @NotNull String targetType;
    protected final @Nullable LootCondition ifTrueCondition;
    protected final @Nullable LootCondition ifFalseCondition;

    public TargetCheckCondition(@NotNull String target, @NotNull String targetType, @Nullable LootCondition ifTrueCondition, @Nullable LootCondition ifFalseCondition) {
        super(target);
        this.targetType = targetType;
        this.ifTrueCondition = ifTrueCondition;
        this.ifFalseCondition = ifFalseCondition;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Object targetObject = ctx.info().get(target);
        if(targetObject==null) return false;
        if(checkType(targetObject, targetType)){
            return ifTrueCondition != null && ifTrueCondition.test(ctx);
        }
        return ifFalseCondition != null && ifFalseCondition.test(ctx);
    }

    public static boolean checkType(@NotNull Object object, @NotNull String targetType){
        switch (targetType.toLowerCase()){
            case "entity" ->{
                if(object instanceof Entity) return true;
            }
            case "item_stack" ->{
                if(object instanceof ItemStack) return true;
            }
            case "player" ->{
                if(object instanceof Player) return true;
            }
            case "mob" ->{
                if(object instanceof Mob) return true;
            }
            case "animal" ->{
                if(object instanceof Animals) return true;
            }
            case "block" ->{
                if(object instanceof Block) return true;
            }
            case "item" ->{
                if(object instanceof Item) return true;
            }
            case "world" ->{
                if(object instanceof World) return true;
            }
            case "block_state" ->{
                if(object instanceof BlockState) return true;
            }
            case "block_data" ->{
                if(object instanceof BlockData) return true;
            }
        }
        return object.getClass().getSimpleName().equalsIgnoreCase(targetType);
    }
}
