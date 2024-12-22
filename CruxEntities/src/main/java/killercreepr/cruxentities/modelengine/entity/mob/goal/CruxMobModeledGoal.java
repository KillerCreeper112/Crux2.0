package killercreepr.cruxentities.modelengine.entity.mob.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.bone.BoneBehaviorTypes;
import com.ticxo.modelengine.api.model.bone.ModelBone;
import com.ticxo.modelengine.api.model.bone.type.SubHitbox;
import killercreepr.crux.core.Crux;
import killercreepr.cruxentities.entity.mob.goal.CruxMobGoal;
import killercreepr.cruxentities.modelengine.wrapper.IModelEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
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
    public boolean isValidTarget(@NotNull LivingEntity target) {
        if(!super.isValidTarget(target)) return false;
        return !isModelPart(target);
    }

    @Override
    public boolean isValidHitTarget(@NotNull Entity target) {
        return super.isValidHitTarget(target) && !isModelPart(target);
    }

    protected ActiveModel cachedModel;
    protected Collection<String> cachedSubHitboxBones;
    public boolean isModelPart(@NotNull Entity e){
        if(shouldUpdateModelSubHitboxCache()){
            updateModelSubHitboxCache();
        }
        if(cachedSubHitboxBones == null) return false;
        if(cachedModel == null) return false;
        UUID uuid = e.getUniqueId();
        for(String boneID : cachedSubHitboxBones){
            ModelBone bone = cachedModel.getBone(boneID).orElse(null);
            if(bone == null) continue;
            SubHitbox hitbox = bone.getBoneBehavior(BoneBehaviorTypes.SUB_HITBOX).orElse(null);
            if(hitbox == null) continue;
            if(uuid.equals(hitbox.getHitboxEntity().getUniqueId())) return true;
        }
        return false;
    }

    public void updateModelSubHitboxCache(){
        ActiveModel model = getModel();
        if(model == null) return;
        Collection<String> found = new HashSet<>();
        for(ModelBone bone : model.getBones().values()){
            SubHitbox hitbox = bone.getBoneBehavior(BoneBehaviorTypes.SUB_HITBOX).orElse(null);
            if(hitbox == null) continue;
            found.add(bone.getBoneId());
        }
        cachedModel = model;
        cachedSubHitboxBones = found.isEmpty() ? null : found;
    }

    public boolean shouldUpdateModelSubHitboxCache(){
        ActiveModel model = getModel();
        if(model == null) return false;
        if(!model.equals(cachedModel)) return true;

        if(cachedSubHitboxBones == null) return false;
        for(String boneID : cachedSubHitboxBones){
            ModelBone bone = model.getBone(boneID).orElse(null);
            if(bone == null) return true;
            SubHitbox hitbox = bone.getBoneBehavior(BoneBehaviorTypes.SUB_HITBOX).orElse(null);
            if(hitbox == null) return true;
        }
        return false;
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
