package killercreepr.crux.core.entity.holder;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.communication.animation.ActiveAnimatedMsg;
import killercreepr.crux.core.communication.animation.AnimatedMsg;
import killercreepr.crux.core.entity.memory.EntityTickedDataHolder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AnimatedMsgHolder extends EntityTickedDataHolder {
    public static AnimatedMsgHolder animatedMsgHolder(Audience audience){
        if(!(audience instanceof Entity e)) return null;
        return EntityMemory.getOrCreateDataHolder(e, AnimatedMsgHolder.class, mem ->{
            return new AnimatedMsgHolder(mem);
        });
    }

    public static final Key KEY = Crux.key("animated_msg");

    public AnimatedMsgHolder(@NotNull EntityMemory parent) {
        this(KEY, parent);
    }
    public AnimatedMsgHolder(@NotNull Key key, @NotNull EntityMemory parent) {
        super(key, parent);
    }

    protected final List<ActiveAnimatedMsg> activeAnimations = new ArrayList<>();

    public void addAnimation(ActiveAnimatedMsg active){
        activeAnimations.add(active);
    }

    @Override
    public boolean shouldRemoveFromMemory(@Nullable Entity e) {
        return super.shouldRemoveFromMemory(e) || activeAnimations.isEmpty();
    }

    @Override
    public void tick(@NotNull Entity e) {
        activeAnimations.removeIf(animation ->{
            animation.tick();
            if(animation.getMaxLength() >= 0 && animation.getFrame() >= animation.getMaxLength()){
                return true;
            }
            return false;
        });
    }
}
