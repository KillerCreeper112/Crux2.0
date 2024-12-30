package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleFileItemLootPoolObject<T extends ItemLootPoolObject> implements CustomFileItemPoolObject<T>{
    protected final Key key;
    public SimpleFileItemLootPoolObject(Key key) {
        this.key = key;
    }
    @Override
    public @NotNull Key key() {
        return key;
    }
}
