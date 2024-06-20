package killercreepr.crux.tags.ab.test;

import killercreepr.crux.tags.ab.container.StringTagContainer;
import killercreepr.crux.tags.ab.context.FormatPrefix;
import killercreepr.crux.tags.ab.hook.ObjectTag;
import killercreepr.crux.tags.ab.tags.Tags;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTags implements ObjectTag<OfflinePlayer> {
    @Override
    public @NotNull Class<OfflinePlayer> getObjectType() {
        return OfflinePlayer.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return null;//todo FormatPrefix.generic("player_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull OfflinePlayer object, @NotNull Tags tags) {
        return new StringTagContainer(tags)
            .add(Tags.parsed("name", object.getName() +""))
            .add(Tags.parsed("uuid", object.getUniqueId().toString()))
            .add(Tags.parsed("health", object.isOnline()?object.getPlayer().getHealth()+"":"0"))
            ;
    }
}
