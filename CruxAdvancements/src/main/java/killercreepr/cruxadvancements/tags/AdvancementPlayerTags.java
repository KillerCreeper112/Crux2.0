package killercreepr.cruxadvancements.tags;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxadvancements.data.AdvancementTracker;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancementPlayerTags implements ObjectTag<Player> {
    @Override
    public @NotNull Class<Player> getObjectType() {
        return Player.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("player_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull Player p, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("is_tracking_advancement", (args, ctx) ->{
                AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
                if(holder == null) return "false";
                AdvancementTracker tracker = holder.getAdvancementTracker();
                Key manager = Crux.key(ctx.deserializeString(args.get(0)));
                Key advancement = Crux.key(ctx.deserializeString(args.get(1)));
                return tracker.isTracking(manager, advancement) + "";
            }))
            ;
    }
}
