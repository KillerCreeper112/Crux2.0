package killercreepr.cruxadvancements.tags;

import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxadvancements.advancement.icon.CruxAdvancementIcon;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementDisplay;
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
        TagContainer<StringResolver> tag = TagContainer.string()
            .add(Tag.string("item", (ctx, args) -> Base64.getEncoder().encodeToString(object.getItem().serializeAsBytes())))
            ;
        if(object instanceof CrazyAdvancementDisplay display){
            tag.add(Tag.string("description", (ctx, args) -> display.getDescription()))
                .add(Tag.string("title", (ctx, args) -> display.getTitle()))
                .add(Tag.string("background_texture", (ctx, args) -> display.getBackgroundTexture()))
                .add(Tag.string("position_origin", (ctx, args) -> display.getPositionOrigin() + ""))
                .add(Tag.string("visibility", (ctx, args) -> display.getVisibility().toString().toLowerCase()))
                .add(Tag.string("x", (ctx, args) -> display.getX() + ""))
                .add(Tag.string("y", (ctx, args) -> display.getY() + ""))
                .add(Tag.string("frame", (ctx, args) -> display.getFrame().toString().toLowerCase()))
            ;
        }
        return tag;
    }
}
