package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
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

            //todo head database or smth
        });
    }
}
