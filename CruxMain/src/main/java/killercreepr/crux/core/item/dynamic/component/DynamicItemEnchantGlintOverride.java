package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
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
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(meta -> meta.setEnchantmentGlintOverride(parseBool(context)));
    }
}
