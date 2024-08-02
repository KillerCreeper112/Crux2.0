package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import killercreepr.crux.Crux;
import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.tags.context.FormatParserContext;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrazyAdvancementDisplay {
    public static Builder builder(){
        return new Builder();
    }

    protected final @NotNull DynamicItem icon;
    protected final @NotNull String title;
    protected final @NotNull String description;
    protected final @NotNull AdvancementDisplay.AdvancementFrame frame;
    protected final @NotNull AdvancementVisibility visibility;
    protected final @Nullable String backgroundTexture;
    protected final float x;
    protected final float y;
    protected final @Nullable Key positionOrigin;

    public CrazyAdvancementDisplay(@NotNull DynamicItem icon, @NotNull String title, @NotNull String description, @NotNull AdvancementDisplay.AdvancementFrame frame, @NotNull AdvancementVisibility visibility, @Nullable String backgroundTexture, float x, float y, @Nullable Key positionOrigin) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.frame = frame;
        this.visibility = visibility;
        this.backgroundTexture = backgroundTexture;
        this.x = x;
        this.y = y;
        this.positionOrigin = positionOrigin;
    }

    public @NotNull DynamicItem getIcon() {
        return icon;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @NotNull AdvancementDisplay.AdvancementFrame getFrame() {
        return frame;
    }

    public @NotNull AdvancementVisibility getVisibility() {
        return visibility;
    }

    public @Nullable String getBackgroundTexture() {
        return backgroundTexture;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public @Nullable Key getPositionOrigin() {
        return positionOrigin;
    }

    public @NotNull AdvancementDisplay toCrazy(@NotNull CrazyAdvancementManager manager){
        AdvancementDisplay display = new AdvancementDisplay(
            icon.buildItem(FormatParserContext.builder(Crux.FORMAT).build()),
            title,
            description,
            frame,
            backgroundTexture,
            visibility
        );
        if(positionOrigin != null){
            Advancement origin = manager.getOrCreateCrazyAdvancement(positionOrigin);
            display.setPositionOrigin(origin);
        }
        display.setX(x);
        display.setY(y);
        return display;
    }

    public static final class Builder {
        private @NotNull DynamicItem icon;
        private @NotNull String title;
        private @NotNull String description;
        private AdvancementDisplay.@NotNull AdvancementFrame frame;
        private @NotNull AdvancementVisibility visibility;
        private @Nullable String backgroundTexture;
        private float x;
        private float y;
        private @Nullable Key positionOrigin;

        public Builder icon(@NotNull DynamicItem icon) {
            this.icon = icon;
            return this;
        }

        public Builder title(@NotNull String title) {
            this.title = title;
            return this;
        }

        public Builder description(@NotNull String description) {
            this.description = description;
            return this;
        }

        public Builder frame(@NotNull AdvancementDisplay.AdvancementFrame frame) {
            this.frame = frame;
            return this;
        }

        public Builder visibility(@NotNull AdvancementVisibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public Builder backgroundTexture(@Nullable String backgroundTexture) {
            this.backgroundTexture = backgroundTexture;
            return this;
        }

        public Builder x(float x) {
            this.x = x;
            return this;
        }

        public Builder y(float y) {
            this.y = y;
            return this;
        }

        public Builder positionOrigin(@Nullable Key positionOrigin) {
            this.positionOrigin = positionOrigin;
            return this;
        }

        public CrazyAdvancementDisplay build() {
            return new CrazyAdvancementDisplay(icon, title, description, frame, visibility, backgroundTexture, x, y, positionOrigin);
        }
    }
}
