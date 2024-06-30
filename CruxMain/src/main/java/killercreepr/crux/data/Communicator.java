package killercreepr.crux.data;

import killercreepr.crux.tags.container.MergedTagContainer;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Communicator {
    default Communicator use(@NotNull Audience a, @Nullable MergedTagContainer tags){
        OfflinePlayer placeholders;
        if(a instanceof OfflinePlayer d) placeholders = d;
        else placeholders = null;
        return use(a, placeholders, tags);
    }
    default Communicator use(@NotNull Audience a){
        return use(a,  (MergedTagContainer) null);
    }

    default Communicator use(@NotNull Audience a, @Nullable OfflinePlayer placeholders){
        return use(a, placeholders, null);
    }

    Communicator use(@NotNull Audience a, @Nullable OfflinePlayer placeholders, @Nullable MergedTagContainer tags);

    Communicator broadcast(@Nullable MergedTagContainer tags);
}
