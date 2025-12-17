package killercreepr.crux.core.communication.animation;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.entity.holder.AnimatedMsgHolder;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnimatedMsg implements Communicator {
    protected final List<Entry> entries;
    protected final Map<String, TextAnimation> animations;
    protected final NumberProvider animationLength;

    public AnimatedMsg(List<Entry> entries, Map<String, TextAnimation> animations, NumberProvider animationLength) {
        this.entries = entries;
        this.animations = animations;
        this.animationLength = animationLength;
    }

    @Override
    public Communicator use(@NotNull Audience a, @NotNull TextParserContext ctx) {
        AnimatedMsgHolder holder = AnimatedMsgHolder.animatedMsgHolder(a);
        if(holder == null) return this;
        holder.addAnimation(createActive(a, ctx));
        return this;
    }

    @Override
    public Communicator broadcast(@NotNull TextParserContext ctx) {
        return null;
    }

    @Override
    public Communicator playAt(@NotNull Location at) {
        return null;
    }

    @Override
    public Communicator playAt(@NotNull Entity at) {
        return null;
    }

    public ActiveAnimatedMsg createActive(Audience a, TextParserContext ctx){
        return new ActiveAnimatedMsg(
            Set.of(a), entries, animations, ctx,
            animationLength
        );
    }

    public record Entry(Communicator msg, int sendRate, int frame){
    }
}
