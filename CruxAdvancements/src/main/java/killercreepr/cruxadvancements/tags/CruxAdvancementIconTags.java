package killercreepr.cruxadvancements.tags;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxadvancements.advancement.icon.CruxAdvancementIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public class CruxAdvancementIconTags implements ObjectTag<CruxAdvancementIcon> {
    @Override
    public @NotNull Class<CruxAdvancementIcon> getObjectType() {
        return CruxAdvancementIcon.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("advancement_icon_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxAdvancementIcon object, @NotNull TagParser tags) {
        return TagContainer.string()
            .add(Tag.string("item", (ctx, args) -> Base64.getEncoder().encodeToString(object.getItem().serializeAsBytes())))
            ;
    }
}
