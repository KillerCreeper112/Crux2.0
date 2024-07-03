package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

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
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
                return;
            }catch (IllegalArgumentException ignored){}

            //todo head database or smth
        });
    }
}
