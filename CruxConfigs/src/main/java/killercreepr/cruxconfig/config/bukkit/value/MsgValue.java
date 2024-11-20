package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.core.communication.MsgContainer;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MsgValue extends CommonValue<MsgContainer> implements Communicator {
    public MsgValue() {
    }

    public MsgValue(@Nullable MsgContainer defaultValue) {
        super(defaultValue);
    }

    public MsgValue(@Nullable MsgContainer defaultValue, @NotNull String @Nullable [] comments) {
        super(defaultValue, comments);
    }

    public MsgValue(@Nullable MsgContainer defaultValue, @Nullable String path, @NotNull String @Nullable ... comments) {
        super(defaultValue, path, comments);
    }
    /**
     * Convenience method to do: new MsgValue(new MsgContainer(chatMessage))
     */
    public MsgValue(@Nullable String chatMessage, @NotNull String @Nullable ... comments) {
        this(new MsgContainer(chatMessage), comments);
    }

    @Override
    public Communicator use(@NotNull Audience a, @NotNull TextParserContext ctx) {
        MsgContainer msg = value();
        if(msg == null) return null;
        return msg.use(a, ctx);
    }

    @Override
    public @Nullable MsgContainer broadcast(@Nullable MergedTagContainer tags) {
        MsgContainer msg = value();
        if(msg == null) return null;
        return msg.broadcast(tags);
    }

    @Override
    public Communicator broadcast(@NotNull TextParserContext ctx) {
        MsgContainer msg = value();
        if(msg == null) return null;
        return msg.broadcast(ctx);
    }

    @Override
    public Communicator playAt(@NotNull Location at) {
        MsgContainer msg = value();
        if(msg == null) return null;
        return msg.playAt(at);
    }

    @Override
    public Communicator playAt(@NotNull Entity at) {
        MsgContainer msg = value();
        if(msg == null) return null;
        return msg.playAt(at);
    }
}
