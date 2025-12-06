package killercreepr.crux.core.communication.animation;

import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxMath;
import net.kyori.adventure.audience.Audience;

import java.util.Collection;
import java.util.Map;

public class ActiveAnimatedMsg implements Ticked {
    protected final Collection<Audience> audience;
    protected final Collection<AnimatedMsg.Entry> messages;
    protected final Map<String, TextAnimation> textAnimations;
    protected final TextParserContext textCtx;
    protected int frame;
    protected final int maxLength;

    public ActiveAnimatedMsg(Collection<Audience> audience, Collection<AnimatedMsg.Entry> messages, Map<String, TextAnimation> textAnimations, TextParserContext textCtx, NumberProvider maxLength) {
        this.audience = audience;
        this.messages = messages;
        this.textAnimations = textAnimations;
        this.textCtx = TextParserContext.wrapped(
            textCtx,
            TagContainer.merged()
                .add(Tag.string("text_animation_output", (args, ctx) ->{
                    String animation = ctx.deserializeString(args.get(0));
                    int frame = args.has(1) ?
                        (int) CruxMath.evaluate(ctx.deserializeString(args.get(1))) : this.frame;
                    TextAnimation ani = textAnimations.get(animation);
                    return ani == null ? "null" : ani.output(frame, ctx);
                }))
                .add(Tag.string("max_animation_length", (args, ctx) ->{
                    int highest = 0;
                    for (TextAnimation value : textAnimations.values()) {
                        int x = value.calculateEndFrame();
                        if(x > highest) highest = x;
                    }
                    return highest + "";
                }))
        );
        this.maxLength = maxLength.sample(this.textCtx).intValue();
    }

    public Collection<Audience> getAudience() {
        return audience;
    }

    public Collection<AnimatedMsg.Entry> getMessages() {
        return messages;
    }

    public Map<String, TextAnimation> getTextAnimations() {
        return textAnimations;
    }

    public TextParserContext getTextCtx() {
        return textCtx;
    }

    public int getFrame() {
        return frame;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void tick() {
        frame++;
        for (AnimatedMsg.Entry entry : messages) {
            if(entry.sendRate() == -1) continue;
            if(frame == 1 || entry.sendRate() == 0 || frame % entry.sendRate() == 0){
                sendToAudience(entry);
            }
        }
    }

    public void sendToAudience(AnimatedMsg.Entry entry){
        for (Audience aud : audience) {
            entry.msg().use(aud, textCtx);
        }
    }
}
