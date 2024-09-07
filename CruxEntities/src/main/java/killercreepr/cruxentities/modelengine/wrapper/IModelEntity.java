package killercreepr.cruxentities.modelengine.wrapper;

import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.jetbrains.annotations.NotNull;

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
        getModel().getAnimationHandler().playAnimation(id, lerpIn, lerpOut, speed, force);
    }

    default void stopAnimation(@NotNull String id){
        stopAnimation(id, false);
    }

    default void stopAnimation(@NotNull String id, boolean force){
        if(force){
            getModel().getAnimationHandler().forceStopAnimation(id);
        }else getModel().getAnimationHandler().stopAnimation(id);
    }

    default boolean isPlayingAnimation(@NotNull String id){
        return getModel().getAnimationHandler().isPlayingAnimation(id);
    }

    default double getAnimationLength(@NotNull String id){
        IAnimationProperty animationProperty = getModel().getAnimationHandler().getAnimation(id);
        return animationProperty == null ? 0D : animationProperty.getBlueprintAnimation().getLength();
    }

    default @NotNull ModeledEntity getModeledEntity(){
        return getModel().getModeledEntity();
    }

    @NotNull ActiveModel getModel();

    IModelEntity setModel(@NotNull ActiveModel model);
    IModelEntity setBaseEntityVisible(boolean value);
    IModelEntity setModelRotationLocked(boolean value);
    boolean isModelRotationLocked();
    boolean isBaseEntityVisible();

    IModelEntity setLockPitch(boolean value);
    IModelEntity setLockYaw(boolean value);
    boolean isLockPitch();
    boolean isLockYaw();
}
