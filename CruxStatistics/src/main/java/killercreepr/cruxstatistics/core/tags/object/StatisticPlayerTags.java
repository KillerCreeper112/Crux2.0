package killercreepr.cruxstatistics.core.tags.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxstatistics.api.bukkit.BukkitStatisticHolder;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticHolder;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import killercreepr.cruxstatistics.core.registries.CruxStatisticRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatisticPlayerTags implements ObjectTag<Player> {
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
            .add(Tag.string("crux_statistic", (args, ctx) ->{
                Key key = Crux.key(args.get(0));
                CruxStatisticType<?> stat = CruxStatisticRegistries.STATISTIC_TYPE.get(key);
                if(stat == null) return key + " statistic not found";
                CruxStatisticHolder holder = BukkitStatisticHolder.statisticHolder(object);
                return holder.getStatistic(stat) + "";
            }))
            ;
    }
}
