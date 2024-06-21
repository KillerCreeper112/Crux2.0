package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.data.MsgContainer;
import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MsgValue extends CommonValue<MsgContainer> {
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

    public @Nullable MsgContainer use(@NotNull Audience a){
        OfflinePlayer placeholders;
        if(a instanceof OfflinePlayer d) placeholders = d;
        else placeholders = null;
        return use(a, placeholders);
    }

    public @Nullable MsgContainer use(@NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(a, placeholders, null);
    }

    public @Nullable MsgContainer use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags){
        MsgContainer msg = value();
        if(msg == null) return null;
        msg.use(a, placeholders, tags);
        return msg;
    }
}
