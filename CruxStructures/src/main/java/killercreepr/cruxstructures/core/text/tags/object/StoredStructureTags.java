package killercreepr.cruxstructures.core.text.tags.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoredStructureTags implements ObjectTag<StoredStructure> {
    @Override
    public @NotNull Class<StoredStructure> getObjectType() {
        return StoredStructure.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("stored_structure_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull StoredStructure object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("key", (args, ctx) -> object.getParent().key().asString()))
            ;
    }
}
