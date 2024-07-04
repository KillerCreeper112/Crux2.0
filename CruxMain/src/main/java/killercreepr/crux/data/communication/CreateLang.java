package killercreepr.crux.data.communication;

import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
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

    default Communicator use(@NotNull String id, @NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(id, a, placeholders, null);
    }

    Communicator use(@NotNull String id, @NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags);

    Communicator broadcast(@NotNull String id, @Nullable MergedTagContainer tags);

    @Nullable Communicator get(@NotNull String id);
    void put(@NotNull String key, @NotNull Communicator communicator);
}
