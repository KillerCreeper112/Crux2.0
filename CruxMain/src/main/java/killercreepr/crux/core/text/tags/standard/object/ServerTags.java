package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerTags implements ObjectTag<Server> {
    @Override
    public @NotNull Class<Server> getObjectType() {
        return Server.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("server_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull Server server, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("player_count", (args, ctx) -> server.getOnlinePlayers().size() + ""))
            .add(Tag.string("name", (args, ctx) -> server.getName()))
            .add(Tag.string("ip", (args, ctx) -> server.getIp()))
            .add(Tag.string("current_tick", (args, ctx) -> server.getCurrentTick() + ""))
            .add(Tag.string("max_players", (args, ctx) -> server.getMaxPlayers() + ""))
            ;
    }
}
