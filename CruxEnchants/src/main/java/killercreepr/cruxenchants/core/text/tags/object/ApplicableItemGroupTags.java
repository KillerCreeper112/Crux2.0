package killercreepr.cruxenchants.core.text.tags.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxenchants.api.enchant.ApplicableItemGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ApplicableItemGroupTags implements ObjectTag<ApplicableItemGroup> {
    @Override
    public @NotNull Class<ApplicableItemGroup> getObjectType() {
        return ApplicableItemGroup.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("applicable_item_group_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull ApplicableItemGroup object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("symbols", (args, ctx) -> object.formatSymbols()))
            ;
    }
}
