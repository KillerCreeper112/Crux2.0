package killercreepr.cruxstats.core.tags.object;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxstats.api.bukkit.BukkitStatHolder;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatHolder;
import killercreepr.cruxstats.core.registries.CruxStatRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatPlayerTags implements ObjectTag<Player> {
    @Override
    public @NotNull Class<Player> getObjectType() {
        return Player.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("player_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull Player object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("crux_stat", (args, ctx) ->{
                Key key = Crux.key(args.get(0));
                CruxStat stat = CruxStatRegistries.STAT.get(key);
                if(stat == null) return key + " stat not found";
                CruxStatHolder holder = BukkitStatHolder.holder(object);
                return holder.getOrLoadStatValue(stat) + "";
            }))
            ;
    }
}
