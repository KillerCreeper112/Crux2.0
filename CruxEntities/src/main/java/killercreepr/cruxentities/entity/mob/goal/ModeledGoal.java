package killercreepr.cruxentities.entity.mob.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.ticxo.modelengine.api.model.ActiveModel;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class ModeledGoal extends CruxMobGoal {
    protected ActiveModel model;
    public ModeledGoal(@NotNull Mob mob, ActiveModel model) {
        super(mob);
        this.model = model;
    }

    public ModeledGoal(@NotNull GoalKey<Mob> key, @NotNull Mob mob, ActiveModel model) {
        super(key, mob);
        this.model = model;
    }


    protected void playAnimation(@NotNull String id, boolean force){
        playAnimation(id, 0D, 0D, 1D, force);
    }
    /**
     *     @param lerpIn - time taken to interpolate into the animation (in seconds)
     *     @param lerpOut - time taken to interpolate out of the animation (in seconds)
     *     @param speed - raw speed multiplier (1 for default speed)
     *     @param force - if false, the animation would only play if the model is not currently playing the animation, or the animation is in LERPOUT phase
     */
    protected void playAnimation(@NotNull String id,
                                 double lerpIn,
                                 double lerpOut,
                                 double speed,
                                 boolean force){
        model.getAnimationHandler().playAnimation(id, lerpIn, lerpOut, speed, force);
    }

    protected void stopAnimation(@NotNull String id){
        stopAnimation(id, false);
    }

    protected void stopAnimation(@NotNull String id, boolean force){
        if(force){
            model.getAnimationHandler().forceStopAnimation(id);
        }else model.getAnimationHandler().stopAnimation(id);
    }

    protected boolean isPlayingAnimation(@NotNull String id){
        return model.getAnimationHandler().isPlayingAnimation(id);
    }

    public ActiveModel getModel() {
        return model;
    }
}
