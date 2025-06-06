package killercreepr.cruxpotions.core.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxPotionTags implements ObjectTag<CruxPotion> {
    @Override
    public @NotNull Class<CruxPotion> getObjectType() {
        return CruxPotion.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("crux_potion_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxPotion object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("color", (args, ctx) ->{
                Color color = object.getColor();
                return color == null ? "#FFFFFF" : CruxColor.colorToHex(color);
            }))
            .add(Tag.string("name", (args, ctx) ->{
                return object.getName();
            }))
            .add(Tag.string("description", (args, ctx) ->{
                String description = object.getDescription();
                return description == null ? "" : description;
            }))
            .add(Tag.string("category", (args, ctx) ->{
                return object.getCategory().toString().toLowerCase();
            }))
            ;
    }
}
