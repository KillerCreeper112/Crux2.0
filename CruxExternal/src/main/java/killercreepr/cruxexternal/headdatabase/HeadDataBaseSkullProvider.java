package killercreepr.cruxexternal.headdatabase;

import com.destroystokyo.paper.profile.PlayerProfile;
import killercreepr.crux.api.handler.SkullProvider;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HeadDataBaseSkullProvider implements SkullProvider {
    protected HeadDatabaseAPI api;
    public HeadDatabaseAPI api(){
        if(api == null) api = new HeadDatabaseAPI();
        return api;
    }

    @Override
    public @Nullable ItemStack getItemHead(@NotNull String id) {
        ItemStack item = api().getItemHead(id);
        return item;
    }

    @Override
    public @Nullable ItemStack getRandomHead() {
        return api().getRandomHead();
    }

    @Override
    public @Nullable PlayerProfile getProfile(@NotNull String id) {
        return getProfile(getItemHead(id));
    }

    @Override
    public @Nullable PlayerProfile getRandomProfile() {
        return getProfile(getRandomHead());
    }

    public @Nullable PlayerProfile getProfile(@Nullable ItemStack item){
        if(item == null || !(item.getItemMeta() instanceof SkullMeta meta)) return null;
        return meta.getPlayerProfile();
    }
}
