package killercreepr.cruxentities.modelengine.entity.mob.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.ticxo.modelengine.api.model.ActiveModel;
import killercreepr.cruxentities.entity.mob.goal.CruxMobGoal;
import killercreepr.cruxentities.modelengine.wrapper.IModelEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxMobModeledGoal extends CruxMobGoal implements IModelEntity {
    protected ActiveModel model;
    public CruxMobModeledGoal(@NotNull Mob mob, ActiveModel model) {
        super(mob);
        this.model = model;
    }

    public CruxMobModeledGoal(@NotNull GoalKey<Mob> key, @NotNull Mob mob, ActiveModel model) {
        super(key, mob);
        this.model = model;
    }

    @Override
    public @NotNull ActiveModel getModel() {
        return model;
    }

    @Override
    public CruxMobModeledGoal setModel(@Nullable ActiveModel model) {
        this.model = model;
        return this;
    }

    @Override
    public @NotNull Entity entity() {
        return mob;
    }
}
