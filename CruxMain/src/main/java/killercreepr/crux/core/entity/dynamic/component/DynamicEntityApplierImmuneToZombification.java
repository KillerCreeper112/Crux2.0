package killercreepr.crux.core.entity.dynamic.component;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierSingleComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;

public class DynamicEntityApplierImmuneToZombification extends DynamicEntityApplierSingleComponent {
    public DynamicEntityApplierImmuneToZombification(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "immune_to_zombification";
    }

    @Override
    public void apply(@NotNull CruxEntity entity, @NotNull TextParserContext ctx) {
        var e = entity.entity();
        if(e instanceof PiglinAbstract z){
            z.setImmuneToZombification(parseBool(ctx));
        }
    }
}
