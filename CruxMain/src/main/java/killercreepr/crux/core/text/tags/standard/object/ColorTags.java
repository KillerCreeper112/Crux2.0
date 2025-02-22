package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorTags implements ObjectTag<Color> {
    @Override
    public @NotNull Class<Color> getObjectType() {
        return Color.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("color_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull Color color, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("hex", (args, ctx) -> CruxColor.colorToHexPlain(color)))
            .add(Tag.string("add_tint", (args, ctx) ->{
                int tint = (int) CruxMath.evaluate(ctx.deserializeString(args.get(0)));
                return CruxColor.colorToHexPlain(CruxColor.addTint(color, tint));
            }))
            ;
    }
}
