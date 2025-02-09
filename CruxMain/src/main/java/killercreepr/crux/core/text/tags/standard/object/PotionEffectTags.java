package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotionEffectTags implements ObjectTag<PotionEffect> {
    @Override
    public @NotNull Class<PotionEffect> getObjectType() {
        return PotionEffect.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("potion_effect_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull PotionEffect object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("key", (args, ctx) -> object.getType().key() + ""))
            .add(Tag.string("duration", (args, ctx) -> object.getDuration() + ""))
            .add(Tag.string("amplifier", (args, ctx) -> object.getAmplifier() + ""))
            .add(Tag.string("harmful", (args, ctx) -> (object.getType().getCategory() == PotionEffectTypeCategory.HARMFUL) + ""))
            .add(Tag.string("name", (args, ctx) -> PlainTextComponentSerializer.plainText().serialize(Component.translatable(object.getType()))))
            ;
    }
}
