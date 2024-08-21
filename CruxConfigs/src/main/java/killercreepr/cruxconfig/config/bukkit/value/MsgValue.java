package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.data.communication.Communicator;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
    public @Nullable MsgContainer use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags){
        MsgContainer msg = value();
        if(msg == null) return null;
        return msg.use(a, placeholders, tags);
    }

    @Override
    public @Nullable MsgContainer broadcast(@Nullable MergedTagContainer tags) {
        MsgContainer msg = value();
        if(msg == null) return null;
        return msg.broadcast(tags);
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
