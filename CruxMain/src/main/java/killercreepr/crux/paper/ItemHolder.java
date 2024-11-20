package killercreepr.crux.paper;

import killercreepr.crux.api.data.Holder;
import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemHolder extends Holder<ItemStack>, Keyed {
    @Override
    @NotNull
    ItemStack value();
}
