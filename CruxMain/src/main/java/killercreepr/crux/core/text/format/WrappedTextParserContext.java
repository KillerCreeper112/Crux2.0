package killercreepr.crux.core.text.format;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class WrappedTextParserContext implements TextParserContext {
    protected final TextParserContext ctx;
    protected final MergedTagContainer tags;

    public WrappedTextParserContext(TextParserContext ctx, MergedTagContainer tags) {
        this.ctx = ctx;
        this.tags = tags;
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return ctx.serialize(component);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text) {
        return deserialize(text, null);
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text) {
        return deserializeString(text, null);
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list) {
        return deserializeList(list, null);
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list) {
        return deserializeStringList(list, null);
    }

    protected final MergedTagContainer buildTags(@Nullable StringTagProvider tags){
        return tags == null ? this.tags : TagContainer.merged().addAll(this.tags).addAll(tags.getStringTags());
    }
    protected final MergedTagContainer buildTags(@Nullable MergedTagContainer tags){
        return tags == null ? this.tags : TagContainer.merged().addAll(this.tags).addAll(tags);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text, @Nullable StringTagProvider tags) {
        return ctx.deserialize(text, buildTags(tags));
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text, @Nullable StringTagProvider tags) {
        return ctx.deserializeString(text, buildTags(tags));
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list, @Nullable MergedTagContainer tags) {
        return ctx.deserializeList(list, buildTags(tags));
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list, @Nullable MergedTagContainer tags) {
        return ctx.deserializeStringList(list, buildTags(tags));
    }

    @Override
    public @NotNull FormatSerializer getFormat() {
        return ctx.getFormat();
    }

    @Override
    public @Nullable FormatPrefix getPrefix() {
        return ctx.getPrefix();
    }
}
