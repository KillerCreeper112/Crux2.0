package killercreepr.cruxentities.modelengine.wrapper;

import com.ticxo.modelengine.api.animation.BlueprintAnimation;
import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import killercreepr.crux.core.Crux;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public interface IModelEntity extends IDesignEntity{
    default void playAnimation(@NotNull String id, boolean force){
        playAnimation(id, 0D, 0D, 1D, force);
    }
    /**
     *     @param lerpIn - time taken to interpolate into the animation (in seconds)
     *     @param lerpOut - time taken to interpolate out of the animation (in seconds)
     *     @param speed - raw speed multiplier (1 for default speed)
     *     @param force - if false, the animation would only play if the model is not currently playing the animation, or the animation is in LERPOUT phase
     */
    default void playAnimation(@NotNull String id,
                                 double lerpIn,
                                 double lerpOut,
                                 double speed,
                                 boolean force){
        applyModel(model ->{
            model.getAnimationHandler().playAnimation(id, lerpIn, lerpOut, speed, force);
        });
        /*ActiveModel model = getModel();
        if(model == null) return;
        model.getAnimationHandler().playAnimation(id, lerpIn, lerpOut, speed, force);*/
    }

    default void stopAnimation(@NotNull String id){
        stopAnimation(id, false);
    }

    default void stopAnimation(@NotNull String id, boolean force){
        applyModel(model ->{
            if(force){
                model.getAnimationHandler().forceStopAnimation(id);
            }else model.getAnimationHandler().stopAnimation(id);
        });
        /*ActiveModel model = getModel();
        if(model == null) return;
        if(force){
            model.getAnimationHandler().forceStopAnimation(id);
        }else model.getAnimationHandler().stopAnimation(id);*/
    }

    default boolean isPlayingAnimation(@NotNull String id){
        ActiveModel model = getModel();
        if(model == null) return false;
        return model.getAnimationHandler().isPlayingAnimation(id);
    }

    default int getAnimationLengthTicks(@NotNull String id){
        ActiveModel model = getModel();
        if(model == null) return 0;
        BlueprintAnimation animation = model.getBlueprint().getAnimations().get(id);
        return animation == null ? 0 : (int) Math.ceil(animation.getLength() * 20D);
        /*IAnimationProperty animationProperty = model.getAnimationHandler().getAnimation(id);
        return animationProperty == null ? 0 : (int) Math.ceil(animationProperty.getBlueprintAnimation().getLength() * 20D);*/
    }

    IModelEntity model(CompletableFuture<ActiveModel> cache);
    CompletableFuture<ActiveModel> model();
    ModeledEntity getModeledEntity();

    ActiveModel getModel();
    default IModelEntity applyModel(Consumer<ActiveModel> consumer){
        ActiveModel model = getModel();
        if(model == null){
            CompletableFuture<ActiveModel> cache = model();
            if(cache == null) return this;
            cache.whenComplete((mm, throwable) ->{
                if(throwable != null) Crux.log(Level.WARNING, throwable.getMessage());
                consumer.accept(mm);
            });
            return this;
        }
        consumer.accept(model);
        return this;
    }

    IModelEntity setModel(@NotNull ActiveModel model);
    IModelEntity setBaseEntityVisible(boolean value);
    IModelEntity setModelRotationLocked(boolean value);
    boolean isModelRotationLocked();
    boolean isBaseEntityVisible();

    default IModelEntity applyPlayAnimation(String id, Consumer<IAnimationProperty> consumer){
        applyModel((model) -> {
            IAnimationProperty property = model.getAnimationHandler().getAnimation(id);
            if (property == null) {
                property = model.getAnimationHandler().playAnimation(id, 0.0, 0.0, 1.0, true);
            }
            if(property == null) return;
            consumer.accept(property);
        });
        return this;
    }

    default IModelEntity applyAnimation(String id, Consumer<IAnimationProperty> consumer){
        applyModel((model) -> {
            IAnimationProperty property = model.getAnimationHandler().getAnimation(id);
            if (property == null) return;
            consumer.accept(property);
        });
        return this;
    }

    default IModelEntity setLockPitch(boolean value){
        applyModel(model -> model.setLockPitch(value));
        return this;
    }
    default IModelEntity setLockYaw(boolean value){
        applyModel(model -> model.setLockYaw(value));
        return this;
    }

    default boolean isLockPitch(){
        ActiveModel model = getModel();
        if(model == null) return false;
        return model.isLockPitch();
    }
    default boolean isLockYaw(){
        ActiveModel model = getModel();
        if(model == null) return false;
        return model.isLockYaw();
    }
}
