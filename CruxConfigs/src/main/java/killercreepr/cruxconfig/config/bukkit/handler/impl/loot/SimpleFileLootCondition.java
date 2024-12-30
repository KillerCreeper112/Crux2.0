package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.loot.conditions.LootCondition;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleFileLootCondition<T extends LootCondition> implements CustomFileLootCondition<T>{
    protected final Key key;
    public SimpleFileLootCondition(Key key) {
        this.key = key;
    }
    @Override
    public @NotNull Key key() {
        return key;
    }
}
