package killercreepr.crux.api.communication.animation;

import killercreepr.crux.api.text.context.TextParserContext;

public interface TextAnimation {
    String output(int frame, TextParserContext ctx);
    int calculateEndFrame();
}
