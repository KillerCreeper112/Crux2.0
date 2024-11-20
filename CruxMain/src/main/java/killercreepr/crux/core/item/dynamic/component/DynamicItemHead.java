package killercreepr.crux.core.item.dynamic.component;

import com.destroystokyo.paper.profile.PlayerProfile;
import killercreepr.crux.core.Crux;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxBase64;
import killercreepr.crux.core.util.CruxItem;
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
        item.editMeta(SkullMeta.class, meta ->{
            String parsed = parseString(context);
            try{
                UUID uuid = UUID.fromString(parsed);
                OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                meta.setOwningPlayer(p);
                meta.setPlayerProfile(p.getPlayerProfile().update().get());
                return;
            }catch (IllegalArgumentException ignored){} catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(CruxBase64.isBase64(parsed)){
                CruxProfile.editSkullItemFromBase64(parsed, item.item());
                return;
            }

            PlayerProfile profile = Crux.handlers().skullProvider().getProfile(parsed);
            if(profile == null) return;
            meta.setPlayerProfile(profile);
        });
    }
}
