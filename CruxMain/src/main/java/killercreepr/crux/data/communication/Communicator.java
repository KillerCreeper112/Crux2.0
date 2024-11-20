package killercreepr.crux.data.communication;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Communicator {
    static Builder builder(){
        return new MsgContainer.Builder();
    }

    static Communicator message(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound){
        return new MsgContainer(chat, actionBar, title, sound);
    }

    static Communicator chat(@NotNull String... messages){
        return chat(List.of(messages));
    }

    static Communicator chat(@NotNull List<String> messages){
        return new MsgContainer(messages);
    }

    static Communicator actionBar(@NotNull String message){
        return new MsgContainer((String) null, message);
    }

    static Communicator sound(@NotNull CreateSound sound){
        return new MsgContainer((String) null, null, null, sound);
    }

    static Communicator title(@NotNull CreateTitle title){
        return new MsgContainer((String) null, null, title, null);
    }

    default Communicator use(@NotNull Audience a, @Nullable MergedTagContainer tags){
        OfflinePlayer placeholders;
        if(a instanceof OfflinePlayer d) placeholders = d;
        else placeholders = null;
        return use(a, placeholders, tags);
    }
    default Communicator use(@NotNull Audience a){
        return use(a,  (MergedTagContainer) null);
    }
    @Deprecated(forRemoval = true)
    default Communicator use(@NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(a, placeholders, null);
    }

    Communicator use(@NotNull Audience a, @NotNull TextParserContext ctx);

    @Deprecated(forRemoval = true)
    Communicator use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags);

    Communicator broadcast(@Nullable MergedTagContainer tags);
    Communicator broadcast(@NotNull TextParserContext ctx);

    Communicator playAt(@NotNull Location at);
    Communicator playAt(@NotNull Entity at);

    interface Builder{
        @NotNull Communicator build();

        Builder chat(String chat);

        Builder broadcast(boolean broadcast);

        Builder chat(List<String> chat);

        Builder actionBar(String actionBar);

        Builder title(CreateTitle title);

        Builder sound(CreateSound sound);
    }
}
