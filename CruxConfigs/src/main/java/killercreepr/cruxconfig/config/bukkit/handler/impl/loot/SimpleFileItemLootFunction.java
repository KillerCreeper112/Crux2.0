package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.loot.item.ItemLootFunction;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleFileItemLootFunction<T extends ItemLootFunction> implements CustomFileItemLootFunction<T> {
    protected final Key key;

    public SimpleFileItemLootFunction(Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
