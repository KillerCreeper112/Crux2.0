package killercreepr.crux.core.loot.conditions;

import killercreepr.crux.api.loot.LootContext;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class InteractActionCondition extends BaseCondition {
    protected final String action;
    public InteractActionCondition(@NotNull String target, String action) {
        super(target);
        this.action = action;
    }

    public Action parseAction(Object object){
        if(object instanceof Action a) return a;
        if(object instanceof PlayerInteractEvent e) return e.getAction();
        return null;
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Action a = parseAction(ctx.info().get(target));
        if(a == null) return false;

        switch (action.toLowerCase()){
            case "right_click" ->{
                if(!a.isRightClick()) return false;
            }
            case "left_click" ->{
                if(!a.isLeftClick()) return false;
            }
            case "physical" ->{
                if(a != Action.PHYSICAL) return false;
            }
        }

        return true;
    }
}
