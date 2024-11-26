package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

public class DynamicItemMaxDamage extends DynamicSingleValueComponent{
    public DynamicItemMaxDamage(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "max_damage";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(Damageable.class, meta -> meta.setMaxDamage(parseInt(context)));
    }
}
