package killercreepr.crux.tags.context;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.format.Format;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FormatParserContext implements TextParserContext {
    protected final @NotNull Format format;
    protected final @Nullable OfflinePlayer viewer;
    protected final @Nullable FormatPrefix tagsPrefix;
    protected final @Nullable MergedTagContainer tags;

    public FormatParserContext(@NotNull Format format,
                               @Nullable OfflinePlayer viewer,
                               @Nullable FormatPrefix tagsPrefix,
                               @Nullable MergedTagContainer tags) {
        this.format = format;
        this.viewer = viewer;
        this.tagsPrefix = tagsPrefix;
        this.tags = tags;
    }


    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return format.serialize(component);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text) {
        return format.deserialize(text, tags);
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text) {
        return format.deserializeString(text, tags);
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list) {
        return format.deserializeList(list, tags);
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list) {
        return format.deserializeStringList(list, tags);
    }

    @Override
    public @NotNull Format getFormat() {
        return format;
    }

    @Override
    public @Nullable FormatPrefix getPrefix() {
        return tagsPrefix;
    }

    public @Nullable OfflinePlayer getViewer() {
        return viewer;
    }

    public @Nullable FormatPrefix getTagsPrefix() {
        return tagsPrefix;
    }

    public static final class Builder {
        private @NotNull Format format;
        private @Nullable OfflinePlayer viewer;
        private @Nullable FormatPrefix tagsPrefix;
        public Builder(@NotNull Format format) {
            this.format = format;
        }

        public Builder(@NotNull FormatParserContext context){
            this.format = context.getFormat();
            viewer(context.getViewer());
            tagsPrefix(context.getTagsPrefix());
        }

        public Builder format(@NotNull Format format) {
            this.format = format;
            return this;
        }

        public Builder viewer(@Nullable OfflinePlayer viewer) {
            this.viewer = viewer;
            return this;
        }

        public Builder tagsPrefix(@Nullable FormatPrefix tagsPrefix) {
            this.tagsPrefix = tagsPrefix;
            return this;
        }

        public @NotNull FormatParserContext build() {
            return new FormatParserContext(format, viewer, tagsPrefix, null);
        }
    }
}
