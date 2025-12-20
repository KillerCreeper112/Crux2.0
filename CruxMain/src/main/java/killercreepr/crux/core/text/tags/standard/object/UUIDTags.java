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

import java.util.UUID;

public class UUIDTags implements ObjectTag<UUID> {
    @Override
    public @NotNull Class<UUID> getObjectType() {
        return UUID.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("uuid_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull UUID uuid, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("string", (args, ctx) -> uuid.toString()))
            .add(Tag.string("least_significant_bits", (args, ctx) ->{
                return uuid.getLeastSignificantBits() + "";
            }))
            .add(Tag.string("most_significant_bits", (args, ctx) ->{
                return uuid.getMostSignificantBits() + "";
            }))
            ;
    }
}
