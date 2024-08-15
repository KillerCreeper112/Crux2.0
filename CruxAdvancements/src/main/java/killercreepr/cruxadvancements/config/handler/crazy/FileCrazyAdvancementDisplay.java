package killercreepr.cruxadvancements.config.handler.crazy;

import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.util.CruxObjects;
import killercreepr.cruxadvancements.crazy.CrazyAdvancementDisplay;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCrazyAdvancementDisplay implements FileHandler<CrazyAdvancementDisplay> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CrazyAdvancementDisplay object) {
        FileRegistry registry = ctx.getRegistry();
        FileObject o = new FileObject()
            .add("icon", registry.serializeToFileElement(object.getIcon()))
            .add("title", registry.serializeToFileElement(object.getTitle()))
            .add("description", registry.serializeToFileElement(object.getDescription()))
            .add("frame", registry.serializeToFileElement(object.getFrame()))
            .add("visibility", registry.serializeToFileElement(object.getVisibility()))
            ;
        if(object.getBackgroundTexture() != null) o.add("background_texture", registry.serializeToFileElement(object.getBackgroundTexture()));
        o.addProperty("x", object.getX())
            .addProperty("y", object.getY())
        ;
        if(object.getPositionOrigin() != null) o.add("position_origin", registry.serializeToFileElement(object.getPositionOrigin()));

        return o;
    }

    @Override
    public @Nullable CrazyAdvancementDisplay deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        DynamicItem icon = registry.deserializeFromFile(DynamicItem.class, o.get("icon"));
        String title = o.getObject(String.class, "title", "");
        String description = o.getObject(String.class, "description", "");
        AdvancementDisplay.AdvancementFrame frame = registry.deserializeFromFile(AdvancementDisplay.AdvancementFrame.class, o.get("frame"));
        if(frame == null) frame = AdvancementDisplay.AdvancementFrame.GOAL;
        AdvancementVisibility visibility = registry.deserializeFromFile(AdvancementVisibility.class, o.get("visibility"));
        if(visibility == null) visibility = AdvancementVisibility.ALWAYS;

        String backgroundTexture = o.getObject(String.class, "background_texture");
        Float x = o.getObject(Float.class, "x", 0f);
        Float y = o.getObject(Float.class, "y", 0f);
        Key positionOrigin = registry.deserializeFromFile(Key.class, o.get("position_origin"));

        if(CruxObjects.checkNull(icon)) return null;

        return new CrazyAdvancementDisplay(
            icon, title, description, frame, visibility, backgroundTexture,
            x, y, positionOrigin
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crazy_advancement_display";
    }
}
