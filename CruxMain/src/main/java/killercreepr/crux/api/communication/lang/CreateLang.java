package killercreepr.crux.api.communication.lang;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.translation.Translatable;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CreateLang {
    default Communicator use(@NotNull String id, @NotNull Audience a, @Nullable MergedTagContainer tags){
        return use(id, a, TextParserContext.builder().tags(tags).build());
    }
    default Communicator use(@NotNull String id, @NotNull Audience a){
        return use(id, a,  (MergedTagContainer) null);
    }

    Communicator use(@NotNull String id, @NotNull Audience a, @NotNull TextParserContext ctx);

    Communicator broadcast(@NotNull String id, @Nullable MergedTagContainer tags);
    Communicator playAt(@NotNull String id, @NotNull Location at);
    Communicator playAt(@NotNull String id, @NotNull Entity at);

    @Nullable Communicator get(@NotNull String id);
    void put(@NotNull String key, @NotNull Communicator communicator);

    //convenience methods

    default @Nullable Communicator get(@NotNull Translatable id){
        return get(id.translateKey());
    }

    default void put(@NotNull Translatable key, @NotNull Communicator communicator){
        put(key.translateKey(), communicator);
    }

    default Communicator use(@NotNull Translatable id, @NotNull Audience a, @Nullable MergedTagContainer tags){
        return use(id, a, TextParserContext.builder().tags(tags).build());
    }
    default Communicator use(@NotNull Translatable id, @NotNull Audience a){
        return use(id, a,  (MergedTagContainer) null);
    }

    default Communicator use(@NotNull Translatable id, @NotNull Audience a,@NotNull TextParserContext ctx){
        return use(id.translateKey(), a, ctx);
    }

    default Communicator broadcast(@NotNull Translatable id, @Nullable MergedTagContainer tags){
        return broadcast(id.translateKey(), tags);
    }
}
