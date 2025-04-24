package killercreepr.cruxenchants.core.text.tags.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxenchants.api.enchant.ApplicableItemGroup;
import killercreepr.cruxenchants.api.enchant.CruxEnchant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CruxEnchantTags implements SimpleObjectTag<CruxEnchant> {
    @Override
    public @NotNull Class<CruxEnchant> getObjectType() {
        return CruxEnchant.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("crux_enchant_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxEnchant object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("max_level", (args, ctx) -> object.maxLevel() + ""))
            .add(Tag.string("description", (args, ctx) -> object.description() + ""))
            .add(Tag.string("key", (args, ctx) -> object.key() + ""))
            .add(Tag.string("name", (args, ctx) -> object.displayName() + ""))
            ;
    }

    @Override
    public @Nullable Map<Object, FormatPrefix> hookObjects(CruxEnchant object) {
        return Map.of(
            object.applicableItemGroup(), FormatPrefix.simple("applicable_group/")
        );
    }
}
