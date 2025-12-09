package killercreepr.crux.core.communication.animation;

import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.api.text.context.TextParserContext;

import java.util.List;

public class CycleTextAnimation implements TextAnimation {
    protected final List<String> texts;
    protected final int delay;

    public CycleTextAnimation(List<String> texts, int delay) {
        this.texts = texts;
        this.delay = delay;
    }

    @Override
    public String output(int frame, TextParserContext ctx) {
        int cycleIndex = (frame / delay) % texts.size();
        return texts.get(cycleIndex);
    }

    @Override
    public int calculateEndFrame() {
        return texts.size() * delay;
    }
}
