package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancementTags implements SimpleObjectTag<Advancement> {
    @Override
    public @NotNull Class<Advancement> getObjectType() {
        return Advancement.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("advancement_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull Advancement item, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("children", (args, context) -> item.getChildren().size() + ""))
            .add(Tag.string("key", (args, context) -> item.key().asString()))
            ;
    }
}
