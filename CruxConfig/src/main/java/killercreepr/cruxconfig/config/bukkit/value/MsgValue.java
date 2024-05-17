package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.data.MsgContainer;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.cruxconfig.config.bukkit.data.MsgContainerValue;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MsgValue extends CfgValue<MsgContainer> {
    public MsgValue(@NotNull ConfigValue<?> type, @NotNull String @Nullable ... comments) {
        super(type, comments);
    }

    public MsgValue(@Nullable String path, @NotNull ConfigValue<?> type, @NotNull String @Nullable ... comments) {
        super(path, type, comments);
    }

    public MsgValue(@Nullable MsgContainer typeValue, @NotNull String @Nullable ... comments) {
        super(new MsgContainerValue(typeValue), comments);
    }

    public MsgValue(@Nullable String path, @Nullable MsgContainer typeValue, @NotNull String @Nullable ... comments) {
        super(path, new MsgContainerValue(typeValue), comments);
    }

    /**
     * Convenience method to do: new MsgValue(new MsgContainer(chatMessage))
     */
    public MsgValue(@Nullable String chatMessage, @NotNull String @Nullable ... comments) {
        this(new MsgContainer(chatMessage), comments);
    }

    public @Nullable MsgContainer use(@NotNull Audience a){
        return use(a, null);
    }

    public @Nullable MsgContainer use(@NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(a, placeholders, null);
    }

    public @Nullable MsgContainer use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable StringHookContainer tags){
        MsgContainer msg = value();
        if(msg == null) return null;
        msg.use(a, placeholders, tags);
        return msg;
    }
}
