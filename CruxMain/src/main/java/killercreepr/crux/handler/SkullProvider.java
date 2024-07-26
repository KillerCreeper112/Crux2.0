package killercreepr.crux.handler;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SkullProvider {
    @Nullable ItemStack getItemHead(@NotNull String id);
    @Nullable
    ItemStack getRandomHead();

    @Nullable
    PlayerProfile getProfile(@NotNull String id);
    @Nullable PlayerProfile getRandomProfile();
}
