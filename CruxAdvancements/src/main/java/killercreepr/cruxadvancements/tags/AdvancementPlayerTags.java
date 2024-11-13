package killercreepr.cruxadvancements.tags;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.data.AdvancementTracker;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.registries.AdvancementRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

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
            .add(Tag.string("tracked_advancements", (args, ctx) ->{
                AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
                if(holder == null) return "0";
                AdvancementTracker tracker = holder.getAdvancementTracker();
                return tracker.getTrackedAdvancements().size() + "";
            }))
            .add(Tag.string("tracked_advancements_max", (args, ctx) ->{
                AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
                if(holder == null) return "0";
                return holder.getMaxTrackedAdvancements() + "";
            }))
            .add(Tag.string("has_completed_advancement", (args, ctx) ->{
                Key manager = Crux.key(args.get(0));
                Key advancement = Crux.key(args.get(1));
                CruxAdvancementManager<?> foundManager = AdvancementRegistries.ADVANCEMENT_MANAGERS.get(manager);
                if(foundManager == null){
                    Crux.log(Level.WARNING, "CruxAdvancementManager not found! " + manager);
                    return "false";
                }
                CruxAdvancement a = foundManager.getAdvancement(advancement);
                if(a == null){
                    Crux.log(Level.WARNING, "CruxAdvancement not found! " + advancement);
                    return "false";
                }
                CruxAdvancementProgress progress = a.getProgressIfPresent(p.getUniqueId());
                return (progress != null && progress.isDone()) + "";
            }))
            ;
    }
}
