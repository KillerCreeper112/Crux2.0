package killercreepr.crux.core.communication.animation;

import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;

import java.util.List;

public class LetterCycleTextAnimation implements TextAnimation {
    protected final String text;
    protected final String rawText;
    protected final List<String> colors;

    protected final int highlightWidth;
    protected final int delay;
    protected final boolean ignoreBlankSpaces;

    protected final int highlightableLen;

    public LetterCycleTextAnimation(
        String rawText,
        String text,
        List<String> colors,
        int highlightWidth,
        int delay,
        boolean ignoreBlankSpaces
    ) {
        this.rawText = rawText;
        this.text = text;
        this.colors = colors;
        this.highlightWidth = Math.max(1, highlightWidth);
        this.delay = Math.max(1, delay);
        this.ignoreBlankSpaces = ignoreBlankSpaces;

        if(ignoreBlankSpaces) {
            int count = 0;
            for(char c : rawText.toCharArray()) {
                if(!Character.isWhitespace(c)) count++;
            }
            this.highlightableLen = Math.max(1, count);
        } else {
            this.highlightableLen = rawText.length();
        }
    }

    @Override
    public String output(int frame, TextParserContext ctx) {
        int visualFrame = frame / delay;
        int startIndex = visualFrame % highlightableLen;
        String color = colors.get(visualFrame % colors.size());

        StringBuilder out = new StringBuilder(rawText.length() * 2);
        int charIndex = 0;

        for (int i = 0; i < rawText.length(); i++) {
            char c = rawText.charAt(i);

            // Skip blank spaces for highlighting
            if(ignoreBlankSpaces && Character.isWhitespace(c)) {
                out.append(c);
                continue; // do not increase charIndex
            }

            boolean highlight = false;
            for (int w = 0; w < highlightWidth; w++) {
                int idx = (startIndex + w) % highlightableLen;
                if (charIndex == idx) {
                    highlight = true;
                    break;
                }
            }

            if (highlight) {
                out.append(
                    ctx.deserializeString(
                        color,
                        TagContainer.string(Tag.parsed("char", String.valueOf(c)))
                    )
                );
            } else {
                out.append(c);
            }

            charIndex++;
        }

        return ctx.deserializeString(text, TagContainer.string(
            Tag.parsed("output", out.toString())
        ));
    }

    public int calculateEndFrame() {
        return (highlightableLen * delay) - 1;
    }

    public int calculateSweepEndFrame() {
        int visualEnd = highlightableLen + highlightWidth - 2;
        return (visualEnd + 1) * delay - 1;
    }
}
