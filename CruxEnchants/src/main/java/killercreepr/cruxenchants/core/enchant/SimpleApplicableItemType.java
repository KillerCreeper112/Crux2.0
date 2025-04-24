package killercreepr.cruxenchants.core.enchant;

import killercreepr.cruxenchants.api.enchant.ApplicableItemType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleApplicableItemType implements ApplicableItemType {
    protected final Key key;
    protected final String symbol;

    public SimpleApplicableItemType(Key key, String symbol) {
        this.key = key;
        this.symbol = symbol;
    }

    @Override
    public @NotNull String symbol() {
        return symbol;
    }
    @Override
    public @NotNull Key key() {
        return key;
    }
}
