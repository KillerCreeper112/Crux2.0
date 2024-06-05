package killercreepr.crux.context;

import killercreepr.crux.tags.container.LoreHookContainer;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.format.FormatPrefix;
import killercreepr.crux.util.CruxItem;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FormatParserContext implements TextParserContext{
    protected final @NotNull Format format;
    protected final @Nullable OfflinePlayer viewer;
    protected final @Nullable FormatPrefix tagsPrefix;
    protected final @Nullable StringHookContainer stringTags;
    protected final @Nullable LoreHookContainer loreTags;

    public FormatParserContext(@NotNull Format format,
                               @Nullable OfflinePlayer viewer,
                               @Nullable FormatPrefix tagsPrefix,
                               @Nullable StringHookContainer stringTags,
                               @Nullable LoreHookContainer loreTags) {
        this.format = format;
        this.viewer = viewer;
        this.tagsPrefix = tagsPrefix;
        this.stringTags = stringTags;
        this.loreTags = loreTags;
    }

    @Override
    public @NotNull String parseString(@NotNull String text) {
        return format.deserializeString(viewer, tagsPrefix, text, stringTags);
    }

    @Override
    public @NotNull List<String> parseStringLore(@NotNull Collection<String> lore) {
        List<String> add = new ArrayList<>();
        for(String s : lore){
            List<String> list = format.deserializeLore(viewer, null, s, loreTags);
            if(list == null){
                list = new ArrayList<>();
                list.add(s);
            }
            for(String input : list){
                add.add(format.deserializeString(viewer, null, input, stringTags));
            }
        }
        return add;
    }

    @Override
    public @NotNull Component parseComponent(@NotNull String text) {
        return format.deserialize(viewer, tagsPrefix, text, stringTags);
    }

    @Override
    public @NotNull List<Component> parseComponentLore(@NotNull Collection<String> lore) {
        List<Component> add = new ArrayList<>();
        for(String s : lore){
            List<String> list = format.deserializeLore(viewer, null, s, loreTags);
            if(list == null){
                list = new ArrayList<>();
                list.add(s);
            }
            for(String input : list){
                add.add(CruxItem.NO_ITALICS
                        .append(format.deserialize(viewer, null, input, stringTags)));
            }
        }
        return add;
    }

    @Override
    public @Nullable List<String> deserializeLore(@NotNull String input) {
        return format.deserializeLore(viewer, tagsPrefix, input, loreTags);
    }

    public @Nullable LoreHookContainer getLoreTags() {
        return loreTags;
    }

    public @NotNull Format getFormat() {
        return format;
    }

    public @Nullable OfflinePlayer getViewer() {
        return viewer;
    }

    public @Nullable FormatPrefix getTagsPrefix() {
        return tagsPrefix;
    }

    public @Nullable StringHookContainer getStringTags() {
        return stringTags;
    }

    public static final class Builder {
        private @NotNull Format format;
        private @Nullable OfflinePlayer viewer;
        private @Nullable FormatPrefix tagsPrefix;
        private @Nullable StringHookContainer stringTags;
        private @Nullable LoreHookContainer loreTags;
        public Builder(@NotNull Format format) {
            this.format = format;
        }

        public Builder(@NotNull FormatParserContext context){
            this(context.getFormat());
            viewer(context.getViewer());
            tagsPrefix(context.getTagsPrefix());
            stringTags(context.getStringTags());
            loreTags(context.getLoreTags());
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

        public Builder stringTags(@Nullable StringHookContainer stringTags) {
            this.stringTags = stringTags;
            return this;
        }

        public Builder loreTags(@Nullable LoreHookContainer loreTags) {
            this.loreTags = loreTags;
            return this;
        }

        public @NotNull FormatParserContext build() {
            return new FormatParserContext(format, viewer, tagsPrefix, stringTags, loreTags);
        }
    }
}
