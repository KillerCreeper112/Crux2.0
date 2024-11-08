package killercreepr.cruxentities.modelengine.entity.mob.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import killercreepr.crux.Crux;
import killercreepr.cruxentities.entity.mob.goal.CruxMobGoal;
import killercreepr.cruxentities.modelengine.wrapper.IModelEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class CruxMobModeledGoal extends CruxMobGoal implements IModelEntity {
    private ActiveModel model;
    private CompletableFuture<ActiveModel> cache;

    public CruxMobModeledGoal(@NotNull Mob mob) {
        super(mob);
    }

    public CruxMobModeledGoal(@NotNull GoalKey<Mob> key, @NotNull Mob mob) {
        super(key, mob);
    }

    public CruxMobModeledGoal model(CompletableFuture<ActiveModel> cache){
        this.cache = cache;
        cache.whenComplete((model, throwable) ->{
            if(throwable != null) Crux.log(Level.WARNING, throwable.getMessage());
            setModel(model);
        });
        return this;
    }

    public CompletableFuture<ActiveModel> model(){
        return cache;
    }

    @Override
    public ModeledEntity getModeledEntity() {
        if(model != null) return model.getModeledEntity();
        return ModelEngineAPI.getOrCreateModeledEntity(mob);
    }

    @Override
    public ActiveModel getModel() {
        return model;
    }

    @Override
    public CruxMobModeledGoal setModel(@Nullable ActiveModel model) {
        this.model = model;
        return this;
    }

    @Override
    public CruxMobModeledGoal setBaseEntityVisible(boolean value) {
        getModeledEntity().setBaseEntityVisible(value);
        return this;
    }

    @Override
    public CruxMobModeledGoal setModelRotationLocked(boolean value) {
        getModeledEntity().setModelRotationLocked(value);
        return this;
    }

    @Override
    public boolean isModelRotationLocked() {
        return getModeledEntity().isModelRotationLocked();
    }

    @Override
    public boolean isBaseEntityVisible() {
        return getModeledEntity().isBaseEntityVisible();
    }

    @Override
    public @NotNull Entity entity() {
        return mob;
    }
}
