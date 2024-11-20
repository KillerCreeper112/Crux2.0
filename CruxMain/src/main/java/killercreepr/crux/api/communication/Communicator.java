package killercreepr.crux.api.communication;

import killercreepr.crux.api.communication.boss.CreateBossBar;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.communication.MsgContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Communicator {
    static Builder builder(){
        return new MsgContainer.Builder();
    }

    static Communicator message(@Nullable List<String> chat, @Nullable String actionBar, @Nullable CreateTitle title, @Nullable CreateSound sound){
        return new MsgContainer(chat, actionBar, title, sound, null);
    }

    static Communicator chat(@NotNull String... messages){
        return chat(List.of(messages));
    }

    static Communicator chat(@NotNull List<String> messages){
        return builder().chat(messages).build();
    }

    static Communicator actionBar(@NotNull String message){
        return builder().actionBar(message).build();
    }

    static Communicator sound(@NotNull CreateSound sound){
        return builder().sound(sound).build();
    }

    static Communicator title(@NotNull CreateTitle title){
        return builder().title(title).build();
    }

    default Communicator use(@NotNull Audience a, @Nullable MergedTagContainer tags){
        return use(a, TextParserContext.builder().tags(tags).build());
    }
    default Communicator use(@NotNull Audience a){
        return use(a,  (MergedTagContainer) null);
    }

    Communicator use(@NotNull Audience a, @NotNull TextParserContext ctx);

    default Communicator broadcast(@Nullable MergedTagContainer tags){
        return broadcast(TextParserContext.builder().tags(tags).build());
    }
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
        Builder bossBar(CreateBossBar bossBar);
    }
}
