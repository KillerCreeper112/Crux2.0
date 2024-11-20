package killercreepr.crux.data.communication;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CreateLang {
    default Communicator use(@NotNull String id, @NotNull Audience a, @Nullable MergedTagContainer tags){
        OfflinePlayer placeholders;
        if(a instanceof OfflinePlayer d) placeholders = d;
        else placeholders = null;
        return use(id, a, placeholders, tags);
    }
    default Communicator use(@NotNull String id, @NotNull Audience a){
        return use(id, a,  (MergedTagContainer) null);
    }
    @Deprecated(forRemoval = true)
    default Communicator use(@NotNull String id, @NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(id, a, placeholders, null);
    }

    Communicator use(@NotNull String id, @NotNull Audience a, @NotNull TextParserContext ctx);

    @Deprecated(forRemoval = true)
    Communicator use(@NotNull String id, @NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags);

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
        OfflinePlayer placeholders;
        if(a instanceof OfflinePlayer d) placeholders = d;
        else placeholders = null;
        return use(id, a, placeholders, tags);
    }
    default Communicator use(@NotNull Translatable id, @NotNull Audience a){
        return use(id, a,  (MergedTagContainer) null);
    }

    default Communicator use(@NotNull Translatable id, @NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(id, a, placeholders, null);
    }

    default Communicator use(@NotNull Translatable id, @NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags){
        return use(id.translateKey(), a, placeholders, tags);
    }

    default Communicator broadcast(@NotNull Translatable id, @Nullable MergedTagContainer tags){
        return broadcast(id.translateKey(), tags);
    }
}
