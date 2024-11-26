package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

public class DynamicItemDamage extends DynamicSingleValueComponent{
    public DynamicItemDamage(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "damage";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(Damageable.class, meta ->{
            int damage = parseInt(context);
            if(damage == 0) meta.resetDamage();
            else meta.setDamage(damage);
        });
    }
}
