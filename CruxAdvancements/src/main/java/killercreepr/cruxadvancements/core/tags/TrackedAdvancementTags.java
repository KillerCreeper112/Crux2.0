package killercreepr.cruxadvancements.core.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxadvancements.core.data.TrackedAdvancement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TrackedAdvancementTags implements SimpleObjectTag<TrackedAdvancement> {
    @Override
    public @NotNull Class<TrackedAdvancement> getObjectType() {
        return TrackedAdvancement.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("tracked_advancement_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull TrackedAdvancement object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("advancement_key", (args, ctx) -> object.getAdvancementKey().asString()))
            .add(Tag.string("manager_key", (args, ctx) -> object.getManagerKey() + ""))
            ;

    }

    @Override
    public @Nullable Map<Object, FormatPrefix> hookObjects(TrackedAdvancement object) {
        return Map.of(
            object.getAdvancementOrThrow(), FormatPrefix.simple("advance/"),
            object.getManager(), FormatPrefix.simple("manager/")
        );
    }
}
