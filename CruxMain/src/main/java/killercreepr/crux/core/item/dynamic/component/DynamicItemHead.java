package killercreepr.crux.core.item.dynamic.component;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxBase64;
import killercreepr.crux.core.util.CruxProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class DynamicItemHead extends DynamicSingleValueComponent{
    public DynamicItemHead(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "head";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        String parsed = parseString(context);
        if(CruxBase64.isBase64(parsed)){
            item.item().setData(DataComponentTypes.PROFILE,
              ResolvableProfile.resolvableProfile().addProperty(new ProfileProperty("textures", parsed)));
            return;
        }

        item.editMeta(SkullMeta.class, meta ->{
            try{
                UUID uuid = UUID.fromString(parsed);
                OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                meta.setOwningPlayer(p);
                meta.setPlayerProfile(p.getPlayerProfile().update().get());
                return;
            }catch (IllegalArgumentException ignored){} catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            PlayerProfile profile = Crux.handlers().skullProvider().getProfile(parsed);
            if(profile == null) return;
            meta.setPlayerProfile(profile);
        });
    }

    @Override
    public boolean isThreadBlocking() {
        return true;
    }
}
