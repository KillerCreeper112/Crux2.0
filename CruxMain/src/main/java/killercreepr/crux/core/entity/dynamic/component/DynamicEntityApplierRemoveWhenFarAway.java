package killercreepr.crux.core.entity.dynamic.component;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierComponent;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierSingleComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxString;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class DynamicEntityApplierRemoveWhenFarAway extends DynamicEntityApplierSingleComponent {
    public DynamicEntityApplierRemoveWhenFarAway(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "remove_when_far_away";
    }

    @Override
    public void apply(@NotNull CruxEntity entity, @NotNull TextParserContext ctx) {
        if(!(entity.entity() instanceof Mob mob)) return;
        mob.setRemoveWhenFarAway(CruxString.parseBoolean(
            ctx.input(value + "")
        ));
    }
}
