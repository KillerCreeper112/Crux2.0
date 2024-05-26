package killercreepr.crux.item.components;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemEnchantGlintOverride extends DynamicSingleValueComponent{
    public DynamicItemEnchantGlintOverride(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "enchant_glint_override";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull InputContext context) {
        item.editMeta(meta -> meta.setEnchantmentGlintOverride(parseBool(context)));
    }
}
